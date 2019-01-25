package com.flowly4j.core.session;

public enum Status {

    CREATED {
        @Override
        public Boolean isExecutable() {
            return true;
        }
    }, RUNNING {
        @Override
        public Boolean isExecutable() {
            return false;
        }
    }, ERROR {
        @Override
        public Boolean isExecutable() {
            return true;
        }
    }, FINISHED {
        @Override
        public Boolean isExecutable() {
            return false;
        }
    }, BLOCKED {
        @Override
        public Boolean isExecutable() {
            return true;
        }
    }, CANCELLED {
        @Override
        public Boolean isExecutable() {
            return false;
        }
    };

    public abstract Boolean isExecutable();

}