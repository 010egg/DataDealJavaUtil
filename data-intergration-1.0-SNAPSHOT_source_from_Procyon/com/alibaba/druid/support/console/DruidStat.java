// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.support.console;

import java.util.Properties;
import com.sun.tools.attach.AgentInitializationException;
import com.sun.tools.attach.AgentLoadException;
import java.io.File;
import com.sun.tools.attach.AttachNotSupportedException;
import java.io.IOException;
import com.sun.tools.attach.VirtualMachine;
import com.alibaba.druid.support.json.JSONUtils;
import javax.management.ObjectName;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Map;
import javax.management.MBeanServerConnection;
import javax.management.remote.JMXConnector;
import java.io.PrintStream;
import java.util.List;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

public class DruidStat
{
    private static final String LOCAL_CONNECTOR_ADDRESS_PROP = "com.sun.management.jmxremote.localConnectorAddress";
    
    public static void main(final String[] args) throws Exception {
        Option opt = null;
        if (Option.isPrintHelp(args)) {
            Option.printHelp();
            return;
        }
        try {
            opt = Option.parseOptions(args);
        }
        catch (OptionParseException e) {
            Option.printHelp(e.getMessage());
            return;
        }
        printDruidStat(opt);
    }
    
    public static void printDruidStat(final Option option) throws Exception {
        final PrintStream out = option.getPrintStream();
        final String address = loadManagementAgentAndGetAddress(option.getPid());
        final JMXServiceURL jmxUrl = new JMXServiceURL(address);
        final JMXConnector jmxc = JMXConnectorFactory.connect(jmxUrl);
        final MBeanServerConnection jmxConn = jmxc.getMBeanServerConnection();
        if (option.printDataSourceData()) {
            final List<Map<String, Object>> content = (List<Map<String, Object>>)invokeService(jmxConn, 1);
            TabledDataPrinter.printDataSourceData(content, option);
        }
        if (option.printSqlData()) {
            final List<Map<String, Object>> content = (List<Map<String, Object>>)invokeService(jmxConn, 2);
            if (content == null) {
                out.println("\u65e0SqlStat\u7edf\u8ba1\u6570\u636e,\u8bf7\u68c0\u67e5\u662f\u5426\u5df2\u6267\u884c\u4e86SQL");
            }
            else {
                TabledDataPrinter.printSqlData(content, option);
            }
        }
        if (option.printActiveConn()) {
            final List<List<String>> content2 = (List<List<String>>)invokeService(jmxConn, 4);
            if (content2 == null || content2.size() == 0) {
                out.println("\u76ee\u524d\u65e0\u6d3b\u52a8\u4e2d\u7684\u6570\u636e\u5e93\u8fde\u63a5");
            }
            else {
                TabledDataPrinter.printActiveConnStack(content2, option);
            }
        }
    }
    
    public static List<Integer> getDataSourceIds(final Option option) throws Exception {
        final String address = loadManagementAgentAndGetAddress(option.getPid());
        final JMXServiceURL jmxUrl = new JMXServiceURL(address);
        final JMXConnector jmxc = JMXConnectorFactory.connect(jmxUrl);
        final MBeanServerConnection jmxConn = jmxc.getMBeanServerConnection();
        final List<Map<String, Object>> content = (List<Map<String, Object>>)invokeService(jmxConn, 1);
        TabledDataPrinter.printDataSourceData(content, option);
        final List<Integer> result = new ArrayList<Integer>();
        for (final Map<String, Object> dsStat : content) {
            final Integer id = dsStat.get("Identity");
            result.add(id);
        }
        return result;
    }
    
    public static Object invokeService(final MBeanServerConnection jmxConn, final int dataType) throws Exception {
        final String url = Option.getUrl(dataType);
        final ObjectName name = new ObjectName("com.alibaba.druid:type=DruidStatService");
        final String result = (String)jmxConn.invoke(name, "service", new String[] { url }, new String[] { String.class.getName() });
        final Map<String, Object> o = (Map<String, Object>)JSONUtils.parse(result);
        final List<Map<String, Object>> content = o.get("Content");
        return content;
    }
    
    private static String loadManagementAgentAndGetAddress(final int vmid) throws IOException {
        VirtualMachine vm = null;
        final String name = String.valueOf(vmid);
        try {
            vm = VirtualMachine.attach(name);
        }
        catch (AttachNotSupportedException x) {
            throw new IOException(x.getMessage(), x);
        }
        final String home = vm.getSystemProperties().getProperty("java.home");
        String agent = home + File.separator + "jre" + File.separator + "lib" + File.separator + "management-agent.jar";
        File f = new File(agent);
        if (!f.exists()) {
            agent = home + File.separator + "lib" + File.separator + "management-agent.jar";
            f = new File(agent);
            if (!f.exists()) {
                throw new IOException("Management agent not found");
            }
        }
        agent = f.getCanonicalPath();
        try {
            vm.loadAgent(agent, "com.sun.management.jmxremote");
        }
        catch (AgentLoadException x2) {
            throw new IOException(x2.getMessage(), x2);
        }
        catch (AgentInitializationException x3) {
            throw new IOException(x3.getMessage(), x3);
        }
        final Properties agentProps = vm.getAgentProperties();
        final String address = (String)agentProps.get("com.sun.management.jmxremote.localConnectorAddress");
        vm.detach();
        return address;
    }
}
