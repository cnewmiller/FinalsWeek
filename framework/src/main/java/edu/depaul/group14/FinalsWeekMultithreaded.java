package edu.depaul.group14;

import java.util.concurrent.CompletableFuture;

public class FinalsWeekMultithreaded {

    public static class Homework {
        private long num;

        public Homework(final long num) {
            this.num = num;
        }
        public void doHomework() throws InterruptedException {
            Thread.sleep(num);
        }
    }

    public FinalsWeekMultithreaded() {

    }

    public void acceptHomework(Homework homework) {
        try {
            homework.doHomework();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        System.out.println("don't let me push");
    }
}
