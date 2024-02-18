package com.rogue.services.utility;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

public final class DateTimeUtils {
    public static long now() {
        return LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
    }
}
