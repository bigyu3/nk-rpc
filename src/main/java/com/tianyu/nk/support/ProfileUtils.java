package com.tianyu.nk.support;

import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

import java.util.Arrays;

/**
 * 环境工具类
 *
 * @author tianyuguo@yeah.net
 * @date 2024/1/25 10:56
 */
public class ProfileUtils {

    private static final String ENV = "env";
    private static final String ENV_QA = "qa";
    private static final String ENV_TEST = "test";
    private static final String ENV_PRO = "pro";
    private static final String ENV_ONLINE = "online";

    public static String guessFromEnvironment(Environment environment, EnvValues ev) {
        String[] activeProfiles = environment.getActiveProfiles();
        if (activeProfiles == null || activeProfiles.length == 0) {
            String env = environment.getProperty(ENV);
            if (StringUtils.isEmpty(env)) {
                return ev.test();
            }
            if (isPro(env)) {
                return ev.pro();
            }
            if (isQa(env)) {
                return ev.test();
            }
            return ev.test();
        }

        if (isPro(activeProfiles)) {
            return ev.pro();
        }

        if (isQa(activeProfiles)) {
            return ev.test();
        }

        return null;
    }


    private static boolean isPro(String[] activeProfiles) {
        return is(activeProfiles, ENV_PRO) || is(activeProfiles, ENV_ONLINE);
    }

    private static boolean isPro(String env) {
        return ENV_PRO.equalsIgnoreCase(env) || ENV_ONLINE.equalsIgnoreCase(env);
    }

    private static boolean isQa(String[] activeProfiles) {
        return is(activeProfiles, ENV_QA) || is(activeProfiles, ENV_TEST);
    }

    private static boolean isQa(String env) {
        return ENV_QA.equalsIgnoreCase(env) || ENV_TEST.equalsIgnoreCase(env);
    }

    private static boolean is(String[] activeProfiles, String profile) {
        return Arrays.stream(activeProfiles).anyMatch(profile::equalsIgnoreCase);
    }


}
