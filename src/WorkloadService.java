import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Callable;

/**
 * Created with IntelliJ IDEA.
 * User: amikryukov
 * Date: 10/29/13
 */
public class WorkloadService implements Callable<Map<Object, Integer>> {

    long seed;
    Random random;
    List pairs;
    Random executionTime = new Random();
    Map<Object, Integer> map = new HashMap<Object, Integer>();

    public WorkloadService(long seed, List pairs){
        random = new Random(seed);
        this.seed = seed;
        this.pairs = pairs;
        for (Object obj : pairs) {
            map.put(obj, 0);
        }
    }

    @Override
    public Map<Object, Integer> call() {

        for(int i = 0 ; i < 1000; i ++) {
            int index = random.nextInt(pairs.size());
            Object value = pairs.get(index);
            map.put(value, map.get(value) + 1);
            try {
                Thread.sleep((long) (executionTime.nextDouble() * 10));
            } catch (InterruptedException e) {
                System.out.print("!");
            }
        }

        return map;
    }
}
