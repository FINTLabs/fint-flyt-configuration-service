package no.fintlabs.security;

public final class CurrentAuditor {
    private static final ThreadLocal<String> TL = new InheritableThreadLocal<>();
    private CurrentAuditor() {}
    public static void set(String v) { TL.set(v); }
    public static String get()       { return TL.get(); }
    public static void clear()       { TL.remove(); }
}