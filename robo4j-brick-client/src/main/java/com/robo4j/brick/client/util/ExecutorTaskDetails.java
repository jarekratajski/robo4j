/*
 * Copyright (C) 2016. Miroslav Kopecky
 * This ExecutorTaskDetails.java is part of robo4j.
 *
 *     robo4j is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     robo4j is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with robo4j .  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package com.robo4j.brick.client.util;

import java.time.LocalDateTime;

/**
 *
 * Following class stores start time and thread name.
 *  class is the Util class
 *
 * Created by miroslavkopecky on 28/02/16.
 */
public final class ExecutorTaskDetails {

    private String threadName;
    private LocalDateTime localDateTime;

    public ExecutorTaskDetails(String threadName, LocalDateTime localDateTime) {
        this.threadName = threadName;
        this.localDateTime = localDateTime;
    }

    public String getThreadName() {
        return threadName;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    @Override
    public String toString() {
        return "ExecutorTaskDetails{" +
                "threadName='" + threadName + '\'' +
                ", localDateTime=" + localDateTime +
                '}';
    }
}