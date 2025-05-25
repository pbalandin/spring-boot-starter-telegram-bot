package io.github.pbalandin.telegram.bot.postprocessor;

import java.util.Comparator;
import java.util.Map;

public class BotControllerMethodComparator implements Comparator<Map.Entry<String, BotControllerMethod>> {
    @Override
    public int compare(Map.Entry<String, BotControllerMethod> e1, Map.Entry<String, BotControllerMethod> e2) {
        int o1 = e1.getValue().getOrder();
        int o2 = e2.getValue().getOrder();

        if (o1 < 0 && o2 >= 0) return 1;
        if (o1 >= 0 && o2 < 0) return -1;
        return Integer.compare(o1, o2);
    }
}
