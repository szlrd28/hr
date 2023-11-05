package hu.cubix.hr.Szilard.config;


import jdk.jfr.Percentage;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.TreeMap;

@Component
@ConfigurationProperties(prefix = "hr")
public class HrConfigProperties {

    private Salary salary = new Salary();

    public Salary getSalary() {
        return salary;
    }

    public void setSalary(Salary salary) {
        this.salary = salary;
    }

    public static class Salary {

        private Default def = new Default();
        private Smart smart = new Smart();

        public Default getDef() {
            return def;
        }

        public void setDef(Default def) {
            this.def = def;
        }

        public Smart getSmart() {
            return smart;
        }

        public void setSmart(Smart smart) {
            this.smart = smart;
        }

    }

    public static class Default {
        private int percent;

        public int getPercent() {
            return percent;
        }

        public void setPercent(int percent) {
            this.percent = percent;
        }
    }

    public static class Smart {

        private Double limit1;
        private Double limit2;
        private Double limit3;
        private Integer percent1;
        private Integer percent2;
        private Integer percent3;


        private TreeMap<Double, Integer> limits;

        public Double getLimit1() {
            return limit1;
        }

        public void setLimit1(Double limit1) {
            this.limit1 = limit1;
        }

        public Double getLimit2() {
            return limit2;
        }

        public void setLimit2(Double limit2) {
            this.limit2 = limit2;
        }

        public Double getLimit3() {
            return limit3;
        }

        public void setLimit3(Double limit3) {
            this.limit3 = limit3;
        }

        public Integer getPercent1() {
            return percent1;
        }

        public void setPercent1(Integer percent1) {
            this.percent1 = percent1;
        }

        public Integer getPercent2() {
            return percent2;
        }

        public void setPercent2(Integer percent2) {
            this.percent2 = percent2;
        }

        public Integer getPercent3() {
            return percent3;
        }

        public void setPercent3(Integer percent3) {
            this.percent3 = percent3;
        }

        public TreeMap<Double, Integer> getLimits() {
            return limits;
        }

        public void setLimits(TreeMap<Double, Integer> limits) {
            this.limits = limits;
        }

    }
}