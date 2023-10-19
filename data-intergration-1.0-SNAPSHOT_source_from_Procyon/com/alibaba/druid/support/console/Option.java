// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.support.console;

import java.io.PrintStream;

public class Option
{
    public static final int DATA_SOURCE = 1;
    public static final int SQL = 2;
    public static final int ACTIVE_CONN = 4;
    private int printDataType;
    private int pid;
    private int id;
    private int interval;
    private boolean detailPrint;
    private PrintStream printStream;
    
    public Option() {
        this.printDataType = 0;
        this.pid = -1;
        this.id = -1;
        this.interval = -1;
        this.printStream = System.out;
    }
    
    public void addPrintDataType(final int newValue) {
        this.printDataType |= newValue;
    }
    
    public static boolean isPrintHelp(final String[] args) {
        if (args == null) {
            return true;
        }
        for (final String arg : args) {
            if (arg.equals("-help")) {
                return true;
            }
        }
        return false;
    }
    
    public boolean printSqlData() {
        return (this.printDataType & 0x2) == 0x2;
    }
    
    public boolean printDataSourceData() {
        return (this.printDataType & 0x1) == 0x1;
    }
    
    public boolean printActiveConn() {
        return (this.printDataType & 0x4) == 0x4;
    }
    
    public int getPrintDataType() {
        return this.printDataType;
    }
    
    public void setPid(final int pid) {
        this.pid = pid;
    }
    
    public int getPid() {
        return this.pid;
    }
    
    public static String getUrl(final int dataType) {
        switch (dataType) {
            case 2: {
                return "/sql.json";
            }
            case 1: {
                return "/datasource.json";
            }
            case 4: {
                return "/activeConnectionStackTrace.json";
            }
            default: {
                return null;
            }
        }
    }
    
    private static int parsePositiveInt(final String v) {
        try {
            return Integer.parseInt(v);
        }
        catch (NumberFormatException e) {
            return -1;
        }
    }
    
    public static Option parseOptions(final String[] args) throws OptionParseException {
        final Option option = new Option();
        int i = 0;
        if (args.length < 1) {
            throw new OptionParseException("not enough arguments!");
        }
        while (i < args.length) {
            final int v1 = parsePositiveInt(args[i]);
            if (i == args.length - 2 && v1 > 0) {
                final int v2 = parsePositiveInt(args[i + 1]);
                if (v2 > 0) {
                    option.setPid(v1);
                    option.setInterval(v2);
                    break;
                }
                throw new OptionParseException("\u8bf7\u5728\u53c2\u6570\u7684\u6700\u540e\u4f4d\u7f6e\u4e0a \u6307\u5b9a pid \u548c refresh-interval");
            }
            else {
                if (i == args.length - 1) {
                    option.setPid(v1);
                }
                if (args[i].equals("-sql")) {
                    option.addPrintDataType(2);
                }
                else if (args[i].equals("-ds")) {
                    option.addPrintDataType(1);
                }
                else if (args[i].equals("-act")) {
                    option.addPrintDataType(4);
                }
                else if (args[i].equals("-detail")) {
                    option.setDetailPrint(true);
                }
                else if (args[i].equals("-id")) {
                    try {
                        final int id = Integer.parseInt(args[i + 1]);
                        option.setId(id);
                        ++i;
                    }
                    catch (NumberFormatException e) {
                        throw new OptionParseException("id\u53c2\u6570\u5fc5\u987b\u662f\u6574\u6570");
                    }
                }
                ++i;
            }
        }
        if (option.getPrintDataType() == 0) {
            throw new OptionParseException("\u8bf7\u5728{'-sql','-ds','-act'}\u53c2\u6570\u4e2d\u9009\u62e9\u4e00\u4e2a\u6216\u591a\u4e2a");
        }
        if (option.getPid() == -1) {
            throw new OptionParseException("\u8bf7\u5728\u53c2\u6570\u4e2d\u6307\u5b9a pid");
        }
        return option;
    }
    
    public static void printHelp(final String errorMsg) {
        printHelp(System.out, errorMsg);
    }
    
    public static void printHelp() {
        printHelp(System.out, null);
    }
    
    public static void printHelp(final PrintStream out, final String errorMsg) {
        if (errorMsg != null) {
            out.println(errorMsg);
            out.println();
        }
        out.println("Usage: druidStat -help | -sql -ds -act [-detail] [-id id] <pid> [refresh-interval]");
        out.println();
        out.println("\u53c2\u6570: ");
        out.println("  -help             \u6253\u5370\u6b64\u5e2e\u52a9\u4fe1\u606f");
        out.println("  -sql              \u6253\u5370SQL\u7edf\u8ba1\u6570\u636e");
        out.println("  -ds               \u6253\u5370DataSource\u7edf\u8ba1\u6570\u636e");
        out.println("  -act              \u6253\u5370\u6d3b\u52a8\u8fde\u63a5\u7684\u5806\u6808\u4fe1\u606f");
        out.println("  -detail           \u6253\u5370\u7edf\u8ba1\u6570\u636e\u7684\u5168\u90e8\u5b57\u6bb5\u4fe1\u606f");
        out.println("  -id id            \u8981\u6253\u5370\u7684\u6570\u636e\u7684\u5177\u4f53id\u503c");
        out.println("  pid               \u4f7f\u7528druid\u8fde\u63a5\u6c60\u7684jvm\u8fdb\u7a0bid");
        out.println("  refresh-interval  \u81ea\u52a8\u5237\u65b0\u65f6\u95f4\u95f4\u9694, \u4ee5\u79d2\u4e3a\u5355\u4f4d");
        out.println();
        out.println("\u8bf4\u660e: ");
        out.println("  -sql,-ds,-act\u53c2\u6570\u4e2d\u8981\u81f3\u5c11\u6307\u5b9a\u4e00\u79cd\u6570\u636e\u8fdb\u884c\u6253\u5370, \u53ef\u4ee5");
        out.println("    \u7ec4\u5408\u4f7f\u7528, \u6bd4\u5982 -sql -ds \u4e00\u8d77\u7684\u8bdd\u5c31\u6253\u5370\u4e24\u79cd\u7edf\u8ba1\u6570\u636e");
        out.println("  -id id\u53ef\u4ee5\u8ddf -sql \u6216-ds\u7ec4\u5408, \u6bd4\u5982  -sql -id 5 \u6216 -ds -id 1086752");
        out.println("  pid\u5fc5\u9700\u6307\u5b9a, refresh-interval\u53ef\u9009, \u5982\u4e0d\u6307\u5b9a,\u5219\u6253\u5370\u6570\u636e\u540e\u9000\u51fa");
        out.println("  pid\u548crefresh-interval\u53c2\u6570\u5fc5\u9700\u653e\u5728\u547d\u4ee4\u884c\u7684\u6700\u540e, \u5426\u5219\u89e3\u6790\u4f1a\u51fa\u9519");
        out.println();
        out.println("\u4f8b\u5b50: ");
        out.println("  \u6253\u53703983\u8fdb\u7a0b\u7684sql \u7edf\u8ba1\u6570\u636e.");
        out.println("      >druidStat -sql 3983");
        out.println("  \u6253\u53703983\u8fdb\u7a0b\u7684ds\u7edf\u8ba1\u6570\u636e.");
        out.println("      >druidStat -ds 3983");
        out.println("  \u6253\u53703983\u8fdb\u7a0b\u7684sql\u7684id\u4e3a10\u7684\u8be6\u7ec6\u7edf\u8ba1\u6570\u636e.");
        out.println("      >druidStat -sql -id 10 -detail 3983");
        out.println("  \u6253\u53703983\u8fdb\u7a0b\u7684\u5f53\u524d\u6d3b\u52a8\u8fde\u63a5\u7684\u5806\u6808\u4fe1\u606f");
        out.println("      >druidStat -act 3983");
        out.println("  \u6253\u53703983\u8fdb\u7a0b\u7684ds,sql,\u548cact\u4fe1\u606f");
        out.println("      >druidStat -ds -sql -act 3983");
        out.println("  \u6bcf\u96945\u79d2\u81ea\u52a8\u6253\u5370ds\u7edf\u8ba1\u6570\u636e");
        out.println("      >druidStat -ds 3983 5");
    }
    
    public int getId() {
        return this.id;
    }
    
    public void setId(final int id) {
        this.id = id;
    }
    
    public void setDetailPrint(final boolean detailPrint) {
        this.detailPrint = detailPrint;
    }
    
    public boolean isDetailPrint() {
        return this.detailPrint;
    }
    
    public void setInterval(final int interval) {
        this.interval = interval;
    }
    
    public int getInterval() {
        return this.interval;
    }
    
    public void setPrintStream(final PrintStream printStream) {
        this.printStream = printStream;
    }
    
    public PrintStream getPrintStream() {
        return this.printStream;
    }
}
