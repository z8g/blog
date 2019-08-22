package net.zhaoxuyang.blog.util;

import java.lang.invoke.MethodHandles;
import org.beetl.core.Context;
import org.beetl.core.Function;

import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PrintTime implements Function {

    final static Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Override
    public String call(Object[] objects, Context context) {
        Date date = (Date) objects[0];

        Date now = new Date();
        long time = now.getTime() - date.getTime();

        if (time < 5 * 60 * 1000) {
            return "刚刚";
        }

        if (time < 60 * 60 * 1000) {
            return String.format("%d分钟前", time / 60 / 1000);
        }

        if (time < 24 * 60 * 60 * 1000) {
            return String.format("%d小时前", time / 60 / 60 / 1000);
        }

        if (time < 365 * 24 * 60 * 60 * 1000) {
            return String.format("%d天前", time / 24 / 60 / 60 / 1000);
        }

        return String.format("%d年前", time / 365 / 24 / 60 / 60 / 1000);
    }
}
