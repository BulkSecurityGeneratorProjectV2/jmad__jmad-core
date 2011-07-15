// @formatter:off
 /*******************************************************************************
 *
 * This file is part of JMad.
 * 
 * Copyright (c) 2008-2011, CERN. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 ******************************************************************************/
// @formatter:on

package cern.accsoft.steering.jmad.util;

public final class ProcTools {

    private ProcTools() {
        /* only static methods */
    }

    /**
     * @param process the process to test.
     * @return true, if the thread was started before, false otherwise
     */
    public static boolean isRunning(Process process) {
        if (process == null) {
            return false;
        }
        /*
         * XXX some dirty trick: Tries to get the exit value. If this is possible, then the thread already terminated.
         * If its not possible, then the process is still running. TODO better solution.
         */
        try {
            process.exitValue();
            return false;
        } catch (IllegalThreadStateException e) {
            return true;
        }
    }
}
