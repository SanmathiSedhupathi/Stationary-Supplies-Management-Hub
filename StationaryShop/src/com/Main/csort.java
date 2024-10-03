package com.Main;

import com.ShanStationaries.*;
import java.util.*;

public class csort implements Comparator<Details> {
    @Override
    public int compare(Details d1, Details d2) {
        return d1.getName().compareTo(d2.getName());
    }
}
