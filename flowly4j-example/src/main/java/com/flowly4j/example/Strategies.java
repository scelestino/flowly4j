package com.flowly4j.example;

import com.flowly4j.core.tasks.compose.retry.scheduling.ConstantSchedulingStrategy;
import com.flowly4j.core.tasks.compose.retry.scheduling.SchedulingStrategy;
import com.flowly4j.core.tasks.compose.retry.stopping.QuantityStoppingStrategy;
import com.flowly4j.core.tasks.compose.retry.stopping.StoppingStrategy;

public class Strategies {

    public static StoppingStrategy TEN_TIMES = new QuantityStoppingStrategy(10);

    public static SchedulingStrategy TWO_MINUTES = new ConstantSchedulingStrategy(120);

}
