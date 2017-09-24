import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.function.BiPredicate;
import java.util.function.Consumer;

public class BinarySearchTree<Key> implements SearchTree <Key> {

    private BSTNode<Key> root;
    private BiPredicate<Key, Key> less_than;

    public BinarySearchTree(BiPredicate<Key, Key> lt) {
        less_than = lt;
        root = null;
    }

    @Override
    public BSTNode<Key> insert(Key key) {
        if(root == null){
            root = new BSTNode(key, null, null);
            return root;
        }else {
            BSTNodePair pair = insert_helper(root, key);
            updateOrders();
            return pair.newNode;
        }
    }

    /*
     * The newNode of BSTNodePair preserve the new node through the whole recursive process.
     */
    private BSTNodePair insert_helper(BSTNode<Key> n, Key key) {
        if (n == null) {
            BSTNode newNode = new BSTNode(key, null, null);
            return new BSTNodePair(newNode, newNode);
        } else if (less_than.test(key, n.key)) {
            BSTNodePair pair = insert_helper(n.left, key);
            n.left = pair.current;
            return new BSTNodePair(n, pair.newNode);
        } else {
            BSTNodePair pair = insert_helper(n.right, key);
            n.right = pair.current;
            return new BSTNodePair(n, pair.newNode);
        }
    }


    @Override
    public BSTNode<Key> search(Key key) {
        return search_helper(root, key);
    }

    private BSTNode<Key> search_helper(BSTNode<Key> n, Key key) {
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

        delete_helper(root, key);
        updateOrders();
    }
    private BSTNode delete_helper(BSTNode<Key> n, Key key){
        if (n == null) {
            return null;
        }else if (less_than.test(key, n.key) ) { // remove in left subtree
            n.left = delete_helper(n.left, key);
            return n;
        } else if (key == n.key) { // remove this node
            if (n.left == null) {
                return n.right;
            } else if (n.right == null) {
                return n.left;
            } else { // two children, replace this with min of right subtree
                BSTNode<Key> min = get_min(n.right);
                n.key = min.key;
                n.right = delete_min(n.right);
                return n;
            }
        } else { // remove in right subtree
            n.right = delete_helper(n.right, key);
            return n;
        }
    }

    private BSTNode<Key> delete_min(BSTNode<Key> n) {
        if (n.left == null) {
            return n.right;
        } else {
            n.left = delete_min(n.left);
            return n;
        }
    }
    private BSTNode<Key> get_min(BSTNode<Key> n) {
        if (n.left == null) {
            return n;
        }
        else {
            return get_min(n.left);
        }
    }

    //update every node's above and below in the tree
    private void updateOrders(){
        ArrayList<BSTNode> inorders = new ArrayList<BSTNode>();
        this.in_order((BSTNode n) -> { inorders.add(n); });
        for (int i = 0; i < inorders.size(); i++) {
            if( i+1 < inorders.size())
                inorders.get(i).above = inorders.get(i+1);
            if(i-1 >= 0)
                inorders.get(i).below = inorders.get(i-1);
        }
    }

    private void in_order(Consumer<BSTNode> consumer) {
        in_order_helper(root, consumer);
    }

    private void in_order_helper(BSTNode n, Consumer<BSTNode> consumer) {
        if (n == null) {
            return;
        } else {
            in_order_helper(n.left, consumer);
            consumer.accept(n);
            in_order_helper(n.right, consumer);
        }
    }

    //test insert, search, delete, after, before
    public static void main(String[] args){
        BiPredicate<Integer, Integer> less_than = (x, y) -> x < y;
        BinarySearchTree<Integer> tree = new BinarySearchTree<Integer>(less_than);

        int[] data = new int[]{5,3,2,9,7,8,10,11};
        for(int i=0; i<data.length; i++){
            BSTNode node = tree.insert(data[i]);
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

        BSTNode n = tree.search(0);
        if(n!=null)
            System.out.println(n.key);
        else{
            System.out.println("search null");
        }

        tree.delete(8);
        tree.print_tree();
        System.out.println();


    }

    //for testing
    public void print_tree() { System.out.print(tree_to_string(root)); }
    private String tree_to_string(BSTNode n) {
        if (n != null) {
            return String.format("(%s %d %s)",
                    tree_to_string(n.left),
                    n.key,
                    tree_to_string(n.right));
        }
        return "";
    }
}

class BSTNode<Key> implements Node<Key>{
    Key key;
    BSTNode left;
    BSTNode right;
    BSTNode above=null;
    BSTNode below=null;

    public BSTNode(Key k, BSTNode l, BSTNode r){
        this.key = k;
        left = l;
        right = r;
    }

    @Override
    public BSTNode<Key> after() {
        return above;
    }

    @Override
    public BSTNode<Key> before() {
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
class BSTNodePair{
    BSTNode current;
    BSTNode newNode;
    public BSTNodePair(BSTNode c, BSTNode newN){
        this.current = c;
        this.newNode = newN;
    }
}

