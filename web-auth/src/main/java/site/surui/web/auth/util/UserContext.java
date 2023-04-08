package site.surui.web.auth.util;

public class UserContext {
    private static final ThreadLocal<String> LOCAL = new ThreadLocal<>();
    public static void put(String token){
        LOCAL.set(token);
    }
    public static String get(){
        return LOCAL.get();
    }
    public static void remove(){
        LOCAL.remove();
    }
}
