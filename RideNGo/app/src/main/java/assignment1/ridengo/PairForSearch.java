package assignment1.ridengo;

/**
 * This class makes a pair object that holds its value and index of its original location in
 * the ride request list. This allows the sorting of the values from lowest to highest, while also
 * having the original index stored.
 * Accessed on November 24, 2016
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