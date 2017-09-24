import java.util.ArrayList;
import java.util.List;

public class Merge {

	public List<Integer> mergeSort(List<Integer> list) {
		if (list.size() <= 1)
			return list;
		else {
			int middle = (int) list.size() / 2;
			List<Integer> left = new ArrayList<Integer>();
			List<Integer> right = new ArrayList<Integer>();
			for (Integer integer : list.subList(0, middle)) {
				left.add(integer);
			}
			for (Integer integer : list.subList(middle, list.size())) {
				right.add(integer);
			}
			left = mergeSort(left);
			right = mergeSort(right);
			return merge(left, right);
		}
	}
	
	public List<Integer> merge(List<Integer> left, List<Integer> right) {
		List<Integer> result = new ArrayList<>();
		int leftIndex = 0, rightIndex = 0;
		Integer temp = 0;

		while (result.size() < (left.size() + right.size())) {

			if (left.size() > leftIndex && right.size() > rightIndex) {
				if (left.get(leftIndex) < right.get(rightIndex)) {
					result.add(left.get(leftIndex++));
				} else {
					result.add(right.get(rightIndex++));
				}
			} else if (left.size() > leftIndex) {
				result.add(left.get(leftIndex++));
			} else if (right.size() > rightIndex) {
				result.add(right.get(rightIndex++));
			}
		}

		return result;
	}
	
}
