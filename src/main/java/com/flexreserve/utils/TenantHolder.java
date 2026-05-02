package com.flexreserve.utils;

public class TenantHolder {
    private static final ThreadLocal<Long> TL = new ThreadLocal<>();


    public static void setTenantId(Long tenantId) {
        TL.set(tenantId);
    }
    public static Long getTenantId() {
        return TL.get();
    }
    public static void remove() {
        TL.remove();
    }
}
