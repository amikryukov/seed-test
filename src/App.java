import java.util.*;
import java.util.concurrent.*;

/**
 * Created with IntelliJ IDEA.
 * User: amikryukov
 * Date: 10/29/13
 * Time: 12:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class App {

    static List pairs = Arrays.asList(1,2,3,4,5,6,7,8,9,0);

    static final int THREAD_NUMBER = 10;

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        ExecutorService ex = Executors.newFixedThreadPool(THREAD_NUMBER);
        List<Future<Map<Object, Integer>>> futureList = new ArrayList<Future<Map<Object, Integer>>>();

        long seed = 1;
        for (int i = 0; i < THREAD_NUMBER; i ++) {
            //!!  add thread with seed incremented.
            Future<Map<Object, Integer>> future = ex.submit(new WorkloadService(seed++, pairs));
            futureList.add(future);
        }
        ex.shutdown();

        Map<Object, Integer> resultMap = new HashMap<Object, Integer>();
        for (Object obj : pairs) {
            resultMap.put(obj, 0);
        }

        for (Future<Map<Object, Integer>> future : futureList) {
            Map<Object, Integer> fMap = future.get();
            for (Map.Entry<Object, Integer> entry : fMap.entrySet()) {
                resultMap.put(entry.getKey(), resultMap.get(entry.getKey()) + entry.getValue());
            }
        }
        System.out.println("Values : " + resultMap.values());

        int sum = 0;
        for (int re : resultMap.values()) {
            sum += re;
        }
        int avg = sum / pairs.size();

        System.out.println("Average value : " + avg);

        List<String> persntily = new ArrayList<String>(10);

        List<Integer> values = new ArrayList<Integer>(resultMap.values());
        for (int i = 0 ; i< pairs.size() ; i ++) {
            persntily.add("");
            persntily.set(i, ((0d + values.get(i)) / avg) * 100 + "%");
        }

        System.out.println("percentages : " + persntily);

    }
}
