import java.util.Comparator;

/**
 * Created by black on 16.02.2016.
 */
public class ComparatorChrono implements java.util.Comparator{

        @Override
        public int compare(Object s1, Object s2) {
            long t1 = Math.abs(( (Message)s1).getTimestamp());
            long t2 = Math.abs(( (Message)s2).getTimestamp());
            return (int)(t2-t1);

        }

    }


