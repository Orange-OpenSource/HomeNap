/*
 * --------------------------------------------------------
 * Module Name : binding-upnp
 * Version : 0.1-SNAPSHOT
 *
 * Software Name : HomeNap
 * Version : 0.1-SNAPSHOT
 *
 * Copyright © 28/06/2012 – 31/12/2013 France Télécom
 * This software is distributed under the Apache 2.0 license,
 * the text of which is available at http://www.apache.org/licenses/LICENSE-2.0.html
 * or see the "LICENSE-2.0.txt" file for more details.
 *
 * --------------------------------------------------------
 * File Name   : ${NAME}
 *
 * Created     :
 * Author(s)   : Remi Druilhe
 *
 * Description :
 *
 * --------------------------------------------------------
 */

package com.orange.homenap.localmanager.upnp.holders;

import java.security.SecureRandom;

class UUIDTimer {
    /**
     * Since System.longTimeMillis() returns time from january 1st 1970,
     * and UUIDs need time from the beginning of gregorian calendar
     * (15-oct-1582), need to apply the offset:
     */
    private final static long kClockOffset = 0x01b21dd213814000L;
    /**
     * Also, instead of getting time in units of 100nsecs, we get something
     * with max resolution of 1 msec... and need the multiplier as well
     */
    private final static long kClockMultiplier = 10000;
    private final static long kClockMultiplierL = 10000L;

    private final SecureRandom mRnd;
    private final byte[] mClockSequence = new byte[2];

    private long mLastTimeStamp = 0L;
    private int mClockCounter = 0;

    public UUIDTimer(SecureRandom rnd) {
        mRnd = rnd;

        initTimeStamps();
    }

    private void initTimeStamps() {
        /* Let's also generate the clock sequence now, plus initialize
         * low 8-bits of the internal clock counter (which is used to
         * compensate for lacking accurace; instead of 100nsecs resolution
         * is at best 1 msec, ie. 10000 coarser... so we have to use
         * a counter)
         */
        mRnd.nextBytes(mClockSequence);
        /* Counter is used to further make it slightly less likely that
         * two instances of UUIDGenerator (from separate JVMs as no more
         * than one can be created in one JVM) would produce colliding
         * time-based UUIDs. The practice of using multiple generators,
         * is strongly discouraged, of course, but just in case...
         */
        byte[] tmp = new byte[1];
        mRnd.nextBytes(tmp);
        mClockCounter = tmp[0] & 0xFF;
        mLastTimeStamp = 0L;
    }

    public void getTimestamp(byte[] uuidData) {
        // First the clock sequence:
        uuidData[UUID.INDEX_CLOCK_SEQUENCE] = mClockSequence[0];
        uuidData[UUID.INDEX_CLOCK_SEQUENCE + 1] = mClockSequence[1];

        long now = System.currentTimeMillis();


        /* Unless time goes backwards (ie. bug in currentTimeMillis()),
         * this should never happen. Nonetheless, UUID draft states that
         * if it does happen, clock sequence has to be re-randomized.
         * Fair enough.
         */
        if (now < mLastTimeStamp) {
            initTimeStamps();
        } else if (now == mLastTimeStamp) {
            // Ran 'out of timestamps' for this clock cycle? Have to wait!
            if (mClockCounter == kClockMultiplier) {
                // Should this be randomised now?
                mClockCounter &= 0xFF;
                do {
                    try {
                        Thread.sleep(1L);
                    } catch (InterruptedException ie) {
                    }
                    now = System.currentTimeMillis();
                } while (now == mLastTimeStamp);
                mLastTimeStamp = now;
            }
        } else {
            mClockCounter &= 0xFF;
            mLastTimeStamp = now;
        }

        /* Now, let's translate the timestamp to one UUID needs, 100ns
         * unit offset from the beginning of Gregorian calendar...
         */
        now *= kClockMultiplierL;
        now += kClockOffset;

        // Plus add the clock counter:
        now += mClockCounter;

        /* Time fields are nicely split across the UUID, so can't just
         * linearly dump the stamp:
         */
        int clockHi = (int) (now >>> 32);
        int clockLo = (int) now;

        uuidData[UUID.INDEX_CLOCK_HI] = (byte) (clockHi >>> 24);
        uuidData[UUID.INDEX_CLOCK_HI + 1] = (byte) (clockHi >>> 16);
        uuidData[UUID.INDEX_CLOCK_MID] = (byte) (clockHi >>> 8);
        uuidData[UUID.INDEX_CLOCK_MID + 1] = (byte) clockHi;

        uuidData[UUID.INDEX_CLOCK_LO] = (byte) (clockLo >>> 24);
        uuidData[UUID.INDEX_CLOCK_LO + 1] = (byte) (clockLo >>> 16);
        uuidData[UUID.INDEX_CLOCK_LO + 2] = (byte) (clockLo >>> 8);
        uuidData[UUID.INDEX_CLOCK_LO + 3] = (byte) clockLo;

        ++mClockCounter;
    }
}

