package com.flowly4j.core.session;

/**
 * Session Status
 */
public enum Status {

    /**
     * This session was just created, it never was executed
     */
    CREATED {
        @Override
        public Boolean isExecutable() {
            return true;
        }
    },

    /**
     * This session is running
     */
    RUNNING {
        @Override
        public Boolean isExecutable() {
            return false;
        }
    },

    /**
     * Last execution ended with error
     */
    ERROR {
        @Override
        public Boolean isExecutable() {
            return true;
        }
    },

    /**
     * Last execution needs to be retried
     */
    TO_RETRY {
        @Override
        public Boolean isExecutable() {
            return true;
        }
    },

    /**
     * Last execution ended the workflow
     */
    FINISHED {
        @Override
        public Boolean isExecutable() {
            return false;
        }
    },

    /**
     * Last execution ended blocked
     */
    BLOCKED {
        @Override
        public Boolean isExecutable() {
            return true;
        }
    };

    public abstract Boolean isExecutable();

}