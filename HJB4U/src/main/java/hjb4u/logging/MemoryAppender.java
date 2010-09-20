/*
 * HJB4U is toolchain for creating a HyperJAXB front end for database users.
 * Copyright (C) 2010  NigelB
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package hjb4u.logging;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Layout;
import org.apache.log4j.spi.LoggingEvent;

import java.util.ArrayList;

/**
 * <code>MemoryAppender</code>
 * Date: Aug 20, 2009
 * Time: 7:50:10 PM
 *
 * @author Nigel Bajema
 */
public class MemoryAppender extends AppenderSkeleton {
    private int size = 5000;
    private LoggingEvent[] events;
    private ArrayList<LoggingEvent> _events;
    private int pos;
    private boolean isSized = true;
    private boolean open = true;

    public MemoryAppender(Layout layout, int size) {
        this.size = size;
        if (size > 0) {
            events = new LoggingEvent[size];
        } else {
            _events = new ArrayList<LoggingEvent>();
            isSized = false;
        }
        setLayout(layout);
    }

    protected void append(LoggingEvent event) {
        if (!open) {
            return;
        }

        if (isSized) {
            events[pos] = event;
            pos = (pos + 1) % size;
        } else {
            _events.add(event);
        }
    }

    public boolean requiresLayout() {
        return true;
    }

    public void close() {
        open = false;
    }

    private String getEventString(LoggingEvent event) {
        StringBuffer toRet = new StringBuffer(layout.format(event));
        toRet.append(Layout.LINE_SEP);
        if (layout.ignoresThrowable()) {
            String[] s = event.getThrowableStrRep();
            if (s != null) {
                int len = s.length;
                for (int i = 0; i < len; i++) {
                    toRet.append(s[i]).append(Layout.LINE_SEP);
                }
            }
        }
        return toRet.toString();
    }

    @Override
    public String toString() {
        StringBuffer toRet = new StringBuffer();

        if (layout.getHeader() != layout.getHeader()) {
            toRet.append(layout.getHeader());
        }

        if (isSized) {
            int _pos = pos;
            for (int i = 0; i < events.length; i++) {
                LoggingEvent event = events[_pos];
                _pos = (_pos + 1) % size;
                if (event != null) {
                    toRet.append(getEventString(event));//.append(Layout.LINE_SEP);
                }
            }
        } else {
            for (LoggingEvent event : _events) {
                toRet.append(getEventString(event));//.append(Layout.LINE_SEP);
            }
        }
        if (layout.getFooter() != null) {
            toRet.append(layout.getFooter());
        }
        return toRet.toString();
    }
}
