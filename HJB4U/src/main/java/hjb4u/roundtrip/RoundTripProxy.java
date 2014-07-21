package hjb4u.roundtrip;

import hjb4u.Util;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.URL;

/**
 * Date: 7/21/14
 * Time: 11:25 AM
 *
 * @author NigelB
 */
public class RoundTripProxy implements InvocationHandler {
    private static Logger logger = Logger.getLogger(RoundTripProxy.class);
    private static RoundTripProxy instance = null;
    private Object roundtrip;

    public RoundTripProxy() throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        URL u = RoundTripProxy.class.getClassLoader().getResource(String.format("META-INF/hjb4u/%s", RoundTripInterface.class.getCanonicalName()));
        if(u == null)
        {
            throw new IOException(String.format("Could not find %s plugin provider.", RoundTripInterface.class.getCanonicalName()));
        }
        String cn = new String(Util.readStream(u.openStream()));
        logger.info("Round Trip Class: "+cn);
        roundtrip = Class.forName(cn).newInstance();

    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Method t = roundtrip.getClass().getMethod(method.getName(), method.getParameterTypes());
        return t.invoke(roundtrip);
    }

    public static RoundTripInterface createRoundTrip()  {
        if (instance == null) {
            try {
                instance = new RoundTripProxy();
            }catch(Throwable t)
            {
                logger.error("Could not create RoundTripProxy", t);
                System.err.println("Could not create RoundTripProxy");
                return null;
            }
        }
        return (RoundTripInterface) java.lang.reflect.Proxy.newProxyInstance(
                RoundTripProxy.class.getClassLoader(),
                new Class[]{RoundTripInterface.class}, instance);
    }

    public static void main(String[] args) {
        System.out.println("META-INF/hjb4u/" + RoundTripInterface.class.getCanonicalName());
    }
}
