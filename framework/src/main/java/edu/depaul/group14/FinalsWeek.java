package edu.depaul.group14;

public class FinalsWeek {

    public static class Homework {
        private long num;

        public Homework(final long num) {
            this.num = num;
        }
        public void doHomework() throws InterruptedException {
            Thread.sleep(num);
        }
    }

    public FinalsWeek() {

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
