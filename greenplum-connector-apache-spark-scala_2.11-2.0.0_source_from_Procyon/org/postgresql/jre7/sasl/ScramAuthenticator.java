// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.jre7.sasl;

import org.postgresql.shaded.com.ongres.scram.common.exception.ScramInvalidServerSignatureException;
import org.postgresql.shaded.com.ongres.scram.common.exception.ScramServerErrorException;
import org.postgresql.shaded.com.ongres.scram.common.exception.ScramParseException;
import org.postgresql.shaded.com.ongres.scram.common.exception.ScramException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.logging.Level;
import org.postgresql.shaded.com.ongres.scram.common.stringprep.StringPreparation;
import org.postgresql.shaded.com.ongres.scram.common.stringprep.StringPreparations;
import org.postgresql.util.PSQLException;
import org.postgresql.util.PSQLState;
import org.postgresql.util.GT;
import java.util.ArrayList;
import java.io.IOException;
import org.postgresql.shaded.com.ongres.scram.client.ScramSession;
import org.postgresql.shaded.com.ongres.scram.client.ScramClient;
import org.postgresql.core.PGStream;
import java.util.logging.Logger;

public class ScramAuthenticator
{
    private static final Logger LOGGER;
    private final String user;
    private final String password;
    private final PGStream pgStream;
    private ScramClient scramClient;
    private ScramSession scramSession;
    private ScramSession.ServerFirstProcessor serverFirstProcessor;
    private ScramSession.ClientFinalProcessor clientFinalProcessor;
    
    private void sendAuthenticationMessage(final int bodyLength, final BodySender bodySender) throws IOException {
        this.pgStream.sendChar(112);
        this.pgStream.sendInteger4(4 + bodyLength);
        bodySender.sendBody(this.pgStream);
        this.pgStream.flush();
    }
    
    public ScramAuthenticator(final String user, final String password, final PGStream pgStream) {
        this.user = user;
        this.password = password;
        this.pgStream = pgStream;
    }
    
    public void processServerMechanismsAndInit() throws IOException, PSQLException {
        final List<String> mechanisms = new ArrayList<String>();
        do {
            mechanisms.add(this.pgStream.receiveString());
        } while (this.pgStream.peekChar() != 0);
        final int c = this.pgStream.receiveChar();
        assert c == 0;
        if (mechanisms.size() < 1) {
            throw new PSQLException(GT.tr("No SCRAM mechanism(s) advertised by the server", new Object[0]), PSQLState.CONNECTION_REJECTED);
        }
        try {
            this.scramClient = ScramClient.channelBinding(ScramClient.ChannelBinding.NO).stringPreparation(StringPreparations.NO_PREPARATION).selectMechanismBasedOnServerAdvertised((String[])mechanisms.toArray(new String[0])).setup();
        }
        catch (IllegalArgumentException e) {
            throw new PSQLException(GT.tr("Invalid or unsupported by client SCRAM mechanisms", e), PSQLState.CONNECTION_REJECTED);
        }
        if (ScramAuthenticator.LOGGER.isLoggable(Level.FINEST)) {
            ScramAuthenticator.LOGGER.log(Level.FINEST, " Using SCRAM mechanism {0}", this.scramClient.getScramMechanism().getName());
        }
        this.scramSession = this.scramClient.scramSession("*");
    }
    
    public void sendScramClientFirstMessage() throws IOException {
        final String clientFirstMessage = this.scramSession.clientFirstMessage();
        ScramAuthenticator.LOGGER.log(Level.FINEST, " FE=> SASLInitialResponse( {0} )", clientFirstMessage);
        final String scramMechanismName = this.scramClient.getScramMechanism().getName();
        final byte[] scramMechanismNameBytes = scramMechanismName.getBytes(StandardCharsets.UTF_8);
        final byte[] clientFirstMessageBytes = clientFirstMessage.getBytes(StandardCharsets.UTF_8);
        this.sendAuthenticationMessage(scramMechanismNameBytes.length + 1 + 4 + clientFirstMessageBytes.length, new BodySender() {
            @Override
            public void sendBody(final PGStream pgStream) throws IOException {
                pgStream.send(scramMechanismNameBytes);
                pgStream.sendChar(0);
                pgStream.sendInteger4(clientFirstMessageBytes.length);
                pgStream.send(clientFirstMessageBytes);
            }
        });
    }
    
    public void processServerFirstMessage(final int length) throws IOException, PSQLException {
        final String serverFirstMessage = this.pgStream.receiveString(length);
        ScramAuthenticator.LOGGER.log(Level.FINEST, " <=BE AuthenticationSASLContinue( {0} )", serverFirstMessage);
        try {
            this.serverFirstProcessor = this.scramSession.receiveServerFirstMessage(serverFirstMessage);
        }
        catch (ScramException e) {
            throw new PSQLException(GT.tr("Invalid server-first-message: {0}", serverFirstMessage), PSQLState.CONNECTION_REJECTED, e);
        }
        if (ScramAuthenticator.LOGGER.isLoggable(Level.FINEST)) {
            ScramAuthenticator.LOGGER.log(Level.FINEST, " <=BE AuthenticationSASLContinue(salt={0}, iterations={1})", new Object[] { this.serverFirstProcessor.getSalt(), this.serverFirstProcessor.getIteration() });
        }
        this.clientFinalProcessor = this.serverFirstProcessor.clientFinalProcessor(this.password);
        final String clientFinalMessage = this.clientFinalProcessor.clientFinalMessage();
        ScramAuthenticator.LOGGER.log(Level.FINEST, " FE=> SASLResponse( {0} )", clientFinalMessage);
        final byte[] clientFinalMessageBytes = clientFinalMessage.getBytes(StandardCharsets.UTF_8);
        this.sendAuthenticationMessage(clientFinalMessageBytes.length, new BodySender() {
            @Override
            public void sendBody(final PGStream pgStream) throws IOException {
                pgStream.send(clientFinalMessageBytes);
            }
        });
    }
    
    public void verifyServerSignature(final int length) throws IOException, PSQLException {
        final String serverFinalMessage = this.pgStream.receiveString(length);
        ScramAuthenticator.LOGGER.log(Level.FINEST, " <=BE AuthenticationSASLFinal( {0} )", serverFinalMessage);
        try {
            this.clientFinalProcessor.receiveServerFinalMessage(serverFinalMessage);
        }
        catch (ScramParseException e) {
            throw new PSQLException(GT.tr("Invalid server-final-message: {0}", serverFinalMessage), PSQLState.CONNECTION_REJECTED, e);
        }
        catch (ScramServerErrorException e2) {
            throw new PSQLException(GT.tr("SCRAM authentication failed, server returned error: {0}", e2.getError().getErrorMessage()), PSQLState.CONNECTION_REJECTED, e2);
        }
        catch (ScramInvalidServerSignatureException e3) {
            throw new PSQLException(GT.tr("Invalid server SCRAM signature", new Object[0]), PSQLState.CONNECTION_REJECTED, e3);
        }
    }
    
    static {
        LOGGER = Logger.getLogger(ScramAuthenticator.class.getName());
    }
    
    private interface BodySender
    {
        void sendBody(final PGStream p0) throws IOException;
    }
}
