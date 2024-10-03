package com.Main;

import com.ShanStationaries.*;
import java.util.*;

public class psort implements Comparator<Details> {
    @Override
    public int compare(Details d1, Details d2) {
        return Integer.compare(d1.getPrice(), d2.getPrice());
    }
}
