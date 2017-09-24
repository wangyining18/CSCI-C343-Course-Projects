import java.util.ArrayList;
import java.util.function.BiPredicate;
import java.util.function.Consumer;

public class AVLTree<Key> implements SearchTree<Key>{

    private AVLNode<Key> root;
    private BiPredicate<Key,Key> less_than;

    public AVLTree(BiPredicate<Key,Key> lt) {
        less_than = lt;
        root = null;
    }


    int height(AVLNode n){
        if(n == null)
            return 0;
        return 1 + Math.max(height(n.left), height(n.right));
    }



    @Override
    public AVLNode<Key> insert(Key key) {
        if(root == null){
            root = new AVLNode(key, null, null);
            return root;
        }else {
            AVLNodePair pair = insert_helper(root, key);
            AVLNode newNode = pair.newNode;
            root = pair.current;
            updateOrders();
            return newNode;
        }
    }

    private AVLNodePair insert_helper(AVLNode<Key> n, Key key){
        if (n == null) {
            AVLNode newNode = new AVLNode(key, null, null);
            return new AVLNodePair(newNode, newNode);
        } else if (less_than.test(key, n.key)) {
            AVLNodePair pair = insert_helper(n.left, key);
            n.left = pair.current;

            if (height(n.left) - height(n.right) == 2) {
                if (less_than.test(key, (Key)n.left.key))
                    n = LLRotation(n);
                else
                    n = LRRotation(n);
            }
            return new AVLNodePair(n, pair.newNode);
        } else {
            AVLNodePair pair = insert_helper(n.right, key);
            n.right = pair.current;

            if (height(n.right) - height(n.left) == 2) {
                if (less_than.test(key, (Key)n.right.key))
                    n = RLRotation(n);
                else
                    n = RRRotation(n);
            }
            return new AVLNodePair(n, pair.newNode);
        }
    }

    private AVLNode<Key> LLRotation(AVLNode<Key> node) {
       // System.out.println("LLRotation");
        AVLNode<Key> left;
        left = node.left;
        node.left = left.right;
        left.right = node;
        return left;
    }
    private AVLNode<Key> LRRotation(AVLNode<Key> node) {
       // System.out.println("LRRotation");
        node.left = RRRotation(node.left);
        return LLRotation(node);
    }

    private AVLNode<Key> RRRotation(AVLNode<Key> node){
       // System.out.println("RRRotation");
        AVLNode<Key> right;
        right = node.right;
        node.right = right.left;
        right.left = node;
        return right;
    }
    private AVLNode<Key> RLRotation(AVLNode<Key> node) {
       // System.out.println("RLRotation");
        node.right = LLRotation(node.right);
        return RRRotation(node);
    }

    @Override
    public AVLNode<Key> search(Key key) {
        return search_helper(root,key);
    }
    private AVLNode<Key> search_helper(AVLNode<Key> n, Key key) {
        if(n == null){
            System.out.println("NO found!");
            return null;
        }else if(less_than.test(key, n.key)){
            return search_helper(n.left,key);
        }else if(n.key == key){
            System.out.println("found!");
            return n;
        }else {
            return search_helper(n.right, key);
        }
    }

    @Override
    public void delete(Key key) {
        AVLNode<Key> del = null;
        if((del = search(key)) != null) {
            delete_helper(root, del);
            updateOrders();
        }
    }
    private AVLNode delete_helper(AVLNode<Key> n, AVLNode<Key> del){
        if (n == null) {
            return null;
        }else if (less_than.test(del.key, n.key) ) { // remove in left subtree
            n.left = delete_helper(n.left, del);

            if (height(n.right) - height(n.left) == 2) {
                AVLNode<Key> right =  n.right;
                if (height(right.left) > height(right.right))
                    n = RLRotation(n);
                else
                    n = RRRotation(n);
            }
            return n;
        } else if (del.key == n.key) { // remove this node
            if (n.left == null) {
                return n.right;
            } else if (n.right == null) {
                return n.left;
            } else { // two children, replace this with min of right subtree

                if(height(n.left) <= height(n.right)) {
                    AVLNode<Key> min = get_min(n.right);
                    n.key = min.key;
                    n.right = delete_helper(n.right, min);
                    return n;
                }else{
                    AVLNode<Key> max = get_max(n.left);
                    n.key = max.key;
                    n.left = delete_helper(n.right, max);
                }
                return n;
            }
        } else { // remove in right subtree
            n.right = delete_helper(n.right, del);

            if (height(n.left) - height(n.right) == 2) {
                AVLNode<Key> left =  n.left;
                if (height(left.right) > height(left.left))
                    n = LRRotation(n);
                else
                    n = LLRotation(n);
            }
            return n;
        }
    }

    private AVLNode<Key> get_max(AVLNode<Key> n) {
        if(n.right == null)
            return n;
        else{
            return get_max(n.right);
        }

    }
    private AVLNode<Key> get_min(AVLNode<Key> n) {
        if (n.left == null) {
            return n;
        }
        else {
            return get_min(n.left);
        }
    }


    //update every node's above and below in the tree
    private void updateOrders(){
        ArrayList<AVLNode> inorders = new ArrayList<AVLNode>();
        this.in_order((AVLNode n) -> { inorders.add(n); });
        for (int i = 0; i < inorders.size(); i++) {
            if( i+1 < inorders.size())
                inorders.get(i).above = inorders.get(i+1);
            if(i-1 >= 0)
                inorders.get(i).below = inorders.get(i-1);
        }
    }

    private void in_order(Consumer<AVLNode> consumer) {
        in_order_helper(root, consumer);
    }

    private void in_order_helper(AVLNode n, Consumer<AVLNode> consumer) {
        if (n == null) {
            return;
        } else {
            in_order_helper(n.left, consumer);
            consumer.accept(n);
            in_order_helper(n.right, consumer);
        }
    }

    //for testing
    public void print_tree() { System.out.print(tree_to_string(root)); }
    private String tree_to_string(AVLNode n) {
        if (n != null) {
            return String.format("(%s %d %s)",
                    tree_to_string(n.left),
                    n.key,
                    tree_to_string(n.right));
        }
        return "";
    }

    //test insert, search, delete, after, before
    public static void main(String[] args){
        BiPredicate<Integer, Integer> less_than = (x, y) -> x < y;
        AVLTree<Integer> tree = new AVLTree<Integer>(less_than);

        int[] data = new int[]{4,2,1,9,7,8,5,0,3,6};
        for(int i=0; i<data.length; i++){
            AVLNode node = tree.insert(data[i]);
            System.out.println("insert key: "+node.key);
            tree.print_tree();
            System.out.println();

            if(node.after() != null){
                System.out.println("after: "+node.after().key);
            }else{
                System.out.println("after: null");
            }
            if(node.before()!=null){
                System.out.println("before: "+node.before().key);
            }else{
                System.out.println("before: null");
            }
        }

        AVLNode n = tree.search(5);
        if(n!=null)
            System.out.println(n.key);
        else{
            System.out.println("search null");
        }

        int del = 4;
        tree.delete(del);
        System.out.println("delete "+del);
        tree.print_tree();
        System.out.println();
    }
}


class AVLNode<Key> implements Node<Key>{
    Key key;
    AVLNode left, right;


    AVLNode above=null;
    AVLNode below=null;

    public AVLNode(Key k, AVLNode l, AVLNode r){
        this.key = k;
        left = l;
        right = r;
    }

    @Override
    public AVLNode<Key> after() {
        return above;
    }

    @Override
    public AVLNode<Key> before() {
        return below;
    }

    @Override
    public Key getKey() {
        return key;
    }

}


/**
 * for storing results;
 * When insert, the current is the current node, the newNode is the new node.
 */
class AVLNodePair{
    AVLNode current;
    AVLNode newNode;
    public AVLNodePair(AVLNode c, AVLNode newN){
        this.current = c;
        this.newNode = newN;
    }
}
