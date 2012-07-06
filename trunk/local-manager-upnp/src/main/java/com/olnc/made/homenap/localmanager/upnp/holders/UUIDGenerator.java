/*
 * --------------------------------------------------------
 *  Module Name : local-manager-upnp
 *  Version : 0.1-SNAPSHOT
 *
 *  Software Name : HomeNap
 *  Version : 0.1-SNAPSHOT
 *
 *  Copyright © 28/06/2012 – 28/06/2012 France Télécom
 *  This software is distributed under the Apache 2.0 license,
 *  the text of which is available at http://www.apache.org/licenses/LICENSE-2.0.html
 *  or see the "LICENSE-2.0.txt" file for more details.
 *
 * --------------------------------------------------------
 *  File Name   : UUIDGenerator.java
 *
 *  Created     : 28/06/2012
 *  Author(s)   : Remi Druilhe
 *
 *  Description :
 *
 * --------------------------------------------------------
 */

package com.olnc.made.homenap.localmanager.upnp.holders;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.SecureRandom;

/**
 * UUIDGenerator is the class that contains factory methods for
 * generating UUIDs using one of the three specified 'standard'
 * UUID generation methods:
 * (see <a href="http://www1.ics.uci.edu/~ejw/authoring/uuid-guid/draft-leach-uuids-guids-01.txt">draft-leach-uuids-guids-01.txt</a> for details)
 * <ul>
 * <li>Time-based generation generates UUID using spatial and
 *     temporal uniqueness. Spatial uniqueness is derived from
 *     ethernet address (MAC, 802.1); temporal from system clock.
 *     See the details from the explanation of
 *     {@link #generateTimeBasedUUID} function.
 * <li>Name-based method uses MD5 hash (or, optionally any user-specified
 *     digest method) of the string formed from
 *     a name space and name.
 * <li>Random method uses Java2 API's SecureRandom to produce
 *     cryptographically secure UUIDs.
 * <li>Tag URI - method uses a variation of name-based method; instead of
 *    using a name space UUID and name, a hash (MD5 by default) is calculated
 *    from URI-tag-prefix, 2 obligatory strings (URL, path) and one
 *    optional string (current date). The resulting UUID is still considered
 *    to be 'name-based UUID' as the specification does not have additional
 *    UUID type ids available.
 *    Note that this is a non-standard method and not strictly UUID-'standard'
 *    compliant.
 * </ul>
 *
 * Some comments about performance:
 * <ul>
 * <li>For non-performance critical generation, all methods with default
 *    arguments (default random number generator, default hash algorithm)
 *    should do just fine.
 * <li>When optimizing performance, it's better to use explicit random
 *    number generator and/or hash algorithm; this way global instance
 *    sharing need not be synchronized
 * <li>Which of the 3 methods is fastest? It depends, and the best way
 *    is to just measure performance, discarding the first UUID generated
 *    with the methods. With time-based method, main overhead comes from
 *    synchronization, with name-based (MD5-)hashing, and with random-based
 *    the speed of random-number generator. Additionally, all methods may
 *    imply some overhead when using the shared global random nunber
 *    generator or hash algorithm.
 * <li>When generating the first UUID with random-/time-based methods,
 *    there may be noticeable delay, as the random number generator is
 *    initialized. This can be avoided by either pre-initialising the
 *    random number generator passed (with random-based method), or by
 *    generating a dummy UUID on a separate thread, when starting a
 *    program needs to generate UUIDs at a later point.
 *
 * </ul>
 * @author Naoufel Chraiet, France Telecom R&D/DTL/ASR
 */
public final class UUIDGenerator {
    private final static UUIDGenerator sSingleton = new UUIDGenerator();

    // Random-generator, used by various UUID-generation methods:
    private SecureRandom mRnd = null;
    // Ethernet address for time-based UUIDs:
    private final Object mDummyAddressLock = new Object();
    private byte[] mDummyAddress = null;
    private final Object mTimerLock = new Object();
    private UUIDTimer mTimer = null;

    /**
     * Constructor is private to enforce singleton access.
     */
    private UUIDGenerator() {
    }

    /**
     * Method used for accessing the singleton generator instance.
     */
    public static UUIDGenerator getInstance() {
        return sSingleton;
    }

    /**
     * Method that returns a randomly generated dummy ethernet address.
     * To prevent collision with real addresses, the returned address has
     * the broadcast bit set, ie. it doesn't represent address of any existing
     * NIC.
     *
     * Note that this dummy address will be shared for the lifetime of
     * this UUIDGenerator, ie. only one is ever generated independent of
     * how many times this methods is called.
     *
     * @return Randomly generated dummy ethernet broadcast address.
     */
    public byte[] getDummyAddress() {
        byte[] dummy = null;
        synchronized (mDummyAddressLock) {
            if (mDummyAddress == null) {
                SecureRandom rnd = getRandomNumberGenerator();
                dummy = new byte[6];
                rnd.nextBytes(dummy);
                /* Need to set the broadcast bit to indicate it's not a real
                 * address.
                 */
                dummy[0] |= (byte) 0x80;
                mDummyAddress = dummy;
            }
        }

        return dummy;
    }

    /**
     * Method for getting the shared random number generator used for
     * generating the UUIDs. This way the initialization cost is only
     * taken once; access need not be synchronized (or in cases where
     * it has to, SecureRandom takes care of it); it might even be good
     * for getting really 'random' stuff to get shared access...
     */
    public SecureRandom getRandomNumberGenerator() {
        /* Could be synchronized, but since side effects are trivial
         * (ie. possibility of generating more than one SecureRandom,
         * of which all but one are dumped) let's not add synchronization
         * overhead:
         */
        if (mRnd == null) {
            mRnd = new SecureRandom();
        }
        return mRnd;
    }

    /**
     * Method for generating (pseudo-)random based UUIDs, using the
     * default (shared) SecureRandom object.
     *
     * Note that the first time
     * SecureRandom object is used, there is noticeable delay between
     * calling the method and getting the reply. This is because SecureRandom
     * has to initialize itself to reasonably random state. Thus, if you
     * want to lessen delay, it may be a good idea to either get the
     * first random UUID asynchronously from a separate thread, or to
     * use the other generateRandomBasedUUID passing a previously initialized
     * SecureRandom instance.
     *
     * @return UUID generated using (pseudo-)random based method
     */
    public UUID generateRandomBasedUUID() {
        return generateRandomBasedUUID(getRandomNumberGenerator());
    }

    /**
     * Method for generating (pseudo-)random based UUIDs, using the
     * specified  SecureRandom object. To prevent/avoid delay JDK's
     * default SecureRandom object causes when first random number
     * is generated, it may be a good idea to initialize the SecureRandom
     * instance (on a separate thread for example) when app starts.
     *
     * @param randomGenerator SecureRandom to use for getting the
     *   random number from which UUID will be composed.
     *
     * @return UUID generated using (pseudo-)random based method
     */
    public UUID generateRandomBasedUUID(SecureRandom randomGenerator) {
        byte[] rnd = new byte[16];

        randomGenerator.nextBytes(rnd);

        return new UUID(UUID.TYPE_RANDOM_BASED, rnd);
    }

    /**
     * Method for generating time based UUIDs. Note that this version
     * doesn't use any existing Hardware address (because none is available
     * for some reason); instead it uses randomly generated dummy broadcast
     * address.
     *
     * Note that since the dummy address is only to be created once and
     * shared from there on, there is some synchronization overhead.
     *
     * @return UUID generated using time based method
     */
    public UUID generateTimeBasedUUID() {
        return generateTimeBasedUUID(getDummyAddress());
    }

    /**
     * Method for generating time based UUIDs. Note that this version
     * doesn't use any existing Hardware ethernet address (because
     * it is not accessible in Java); instead it uses the IP address
     * for bytes 0 to 3 and then completed with randomly generated
     * dummy bytes.
     *
     * @return UUID generated using time based method
     */
    public UUID generateTimeBasedUUID(InetAddress IPAddress) {
        byte[] ethaddress = new byte[6];
        byte[] address = IPAddress.getAddress();
        SecureRandom rnd = new SecureRandom();
        byte[] dummy = new byte[2];
        rnd.nextBytes(dummy);
        for (int i = 0; i < 4; i++) {
            ethaddress[i] = address[i];
        }
        ethaddress[4] = dummy[0];
        ethaddress[5] = dummy[1];
        return generateTimeBasedUUID(ethaddress);
    }

    /**
     * Method for generating time based UUIDs.
     *
     * @param ethernetAddr Hardware address (802.1) to use for generating
     *   spatially unique part of UUID. If system has more than one NIC,
     *   any address is usable. If no NIC is available (or its address
     *   not accessible; often the case with java apps), a randomly
     *   generated broadcast address is acceptable. If so, use the
     *   alternative method that takes no arguments.
     *
     * @return UUID generated using time based method
     */
    public UUID generateTimeBasedUUID(byte[] ethernetAddr) {
        byte[] contents = new byte[16];
        for (int i = 10; i < 16; i++)
            contents[i] = ethernetAddr[i - 10];
        synchronized (mTimerLock) {
            if (mTimer == null) {
                mTimer = new UUIDTimer(getRandomNumberGenerator());
            }

            mTimer.getTimestamp(contents);
        }

        return new UUID(UUID.TYPE_TIME_BASED, contents);
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.exit(-1);
        } else {
            UUID uuid = generateNewUUID();
            String line = "# This is generated before service packaging in bundle\n" +
                    "# and added to both service and proxy properties file.\n" +
                    "# This is how the proxy activator can find and teleport the\n" +
                    "# service proxy to the user plateform.\n" +
                    "UUID = " + uuid.toString();
            try {
                for (int i = 0; i < args.length; i++) {
                    appendLineToFile(args[i], line);
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(-1);
            }
        }
    }
    
    /**
	 * Let's generate a new Time based UUID. The ethernet address used for
	 * this purpose is derived from the IP address.
	 * If we fail in getting a 4 byte IP address, or if we get a loopback address
	 * (one that is of type 127.x.x.x), the UUID is generated from a dummy randomized
	 * ethernet address.
	 *
	 * @return UUID an new generated UUID to identify a new instance of Service
	 */
	public static UUID generateNewUUID() {
		UUIDGenerator uuidGenerator = UUIDGenerator.getInstance();
		InetAddress a;
		try {
			a = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			//"Error in getLocalHost(). !! So, UUID is generated randomly !!");
			return (uuidGenerator.generateTimeBasedUUID());
		}
		byte aa[] = a.getAddress();
		if (aa.length != 4) {
			// IP Address of size other than 4 not supported in Pervasif.
			// !! So UUID is generated randomly !!"
			return (uuidGenerator.generateTimeBasedUUID());
		}
		if (aa[0] == 127) {
			// Loopback IP address not allowed in uuid generation : fix /etc/hosts (or equivalent) so that
			// java.net.InetAddress.getLocalHost() does not return 127.x.x.x.
			// !! Otherwise, UUID is generated randomly !!");
			return (uuidGenerator.generateTimeBasedUUID());
		}
		return (uuidGenerator.generateTimeBasedUUID(a));
	}

    private static void appendLineToFile(String filePath, String line) throws Exception {
        BufferedWriter writer;
        try {
            writer = new BufferedWriter(new FileWriter(filePath));
            writer.write(line, 0, line.length());
            writer.flush();
            writer.close();
        } catch (Exception e) {
            throw new Exception("Problems occured when trying to open the file. " + e);
        }
    }
}


