package seerbit.seerbit_coding_interview_challenge.algorithms;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AlgorithmsSolution {
    public boolean hasTwoIntegersWithSum(int[] nums, int target) {
        Set<Integer> set = new HashSet<>();
        for (int num : nums) {
            int complement = target - num;
            if (set.contains(complement)) {
                return true;
            }
            set.add(num);
        }
        return false;
    }
    public int[] findRange(int[] nums, int target) {
        int[] range = new int[] { -1, -1 };
        int left = 0;
        int right = nums.length - 1;

        // Find the left index
        while (left < right) {
            int mid = left + (right - left) / 2;
            if (nums[mid] < target) {
                left = mid + 1;
            } else {
                right = mid;
            }
        }

        if (nums[left] != target) {
            return range;
        }

        range[0] = left;

        // Find the right index
        right = nums.length - 1;
        while (left < right) {
            int mid = left + (right - left + 1) / 2;
            if (nums[mid] > target) {
                right = mid - 1;
            } else {
                left = mid;
            }
        }

        range[1] = right;
        return range;
    }

    public List<Interval> mergeIntervals(List<Interval> intervals) {
        List<Interval> merged = new ArrayList<>();
        if (intervals.isEmpty()) {
            return merged;
        }

        Interval prevInterval = intervals.get(0);
        for (int i = 1; i < intervals.size(); i++) {
            Interval currInterval = intervals.get(i);
            if (prevInterval.end >= currInterval.start) {
                prevInterval.end = Math.max(prevInterval.end, currInterval.end);
            } else {
                merged.add(prevInterval);
                prevInterval = currInterval;
            }
        }
        merged.add(prevInterval);

        return merged;
    }

    class Interval {
        int start;
        int end;

        public Interval(int start, int end) {
            this.start = start;
            this.end = end;
        }
    }


}
