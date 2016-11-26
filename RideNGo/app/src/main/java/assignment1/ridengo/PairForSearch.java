package assignment1.ridengo;

import android.util.Pair;
//import java.io.Serializable;

/**
 * http://stackoverflow.com/questions/23587314/how-to-sort-an-array-and-keep-track-of-the-index-in-java
 */
public class PairForSearch implements Comparable<PairForSearch>, java.io.Serializable {
    public final int index;
    public final double value;

    public PairForSearch(int index, double value){
        this.index = index;
        this.value = value;
    }

    @Override
    public int compareTo(PairForSearch other){
        return Double.valueOf(this.value).compareTo(other.value);
    }

    public int getIndex() {
        return this.index;
    }

    public double getValue() {
        return this.value;
    }
}
