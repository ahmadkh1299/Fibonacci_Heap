import javax.xml.soap.Node;
import java.util.Arrays;
/*
Names:-
- Ahmad Khalaila
- Yaseen Hadba
usernames:-
- ahmadk1
- yaseenhadba
IDs:=
- 207177197
- 208219444
 */
/**
 * FibonacciHeap
 *
 * An implementation of a Fibonacci Heap over integers.
 */
public class FibonacciHeap
{
    public static int totalLinks=0;
    public static int totalCuts=0;

    public int NumOfTrees=0;
    public int NumOfMarked=0;
    public int size=0;
    public HeapNode min;
    public HeapNode first;
    FibonacciHeap(HeapNode x)
    {
        this.min=x;
        this.first=this.min;
        this.size=x.getRank()+1;
        this.NumOfTrees=1;
    }
    FibonacciHeap(){
    }
    /**
     * public boolean isEmpty()
     *
     * Returns true if and only if the heap is empty.
     *
     */
    public boolean isEmpty()
    {
        return this.size==0;
    } //v

    /**
     * public HeapNode insert(int key)
     *
     * Creates a node (of type HeapNode) which contains the given key, and inserts it into the heap.
     * The added key is assumed not to already belong to the heap.
     *
     * Returns the newly created node.
     */
    public HeapNode insert(int key) //v
    {
        HeapNode x=new HeapNode(key);
        FibonacciHeap b0= new FibonacciHeap(x);
        if (this.isEmpty())
        {
            NumOfTrees=1;
            size=1;
            min=x;
            first=x;
            first.setNext(first);
            first.setPrev(first);
            return x;
        }
        x.setPrev(this.first.getPrev());
        this.first.getPrev().setNext(x);
        this.first.setPrev(x);
        x.setNext(this.first);
        this.first=x;
        if(this.min.getKey()>b0.min.getKey())
        {
            this.min=b0.min;
        }
        NumOfTrees++;
        size++;
        return x;
    }

    /**
     * public void deleteMin()
     *
     * Deletes the node containing the minimum key.
     *
     */
    public void deleteMin() //v
    {
        HeapNode minPrev=this.min.getPrev();
        HeapNode minNext=this.min.getNext();
        HeapNode minChild= this.min.getChild();
        if(min.getRank()==0 && NumOfTrees==1)//the heap only contains a single tree and it's an only node
        {
            this.NumOfTrees=0;
            this.first=null;
            this.min=null;
            this.NumOfMarked=0;
            this.size=0;
            return;
        }
        if(min.getRank()==0){
            minPrev.setNext(minNext);
            minNext.setPrev(minPrev);
        }
        else {
            if(this.NumOfTrees!=1) {
                HeapNode minLastChild = minChild.getPrev();
                minPrev.setNext(minChild);
                minChild.setPrev(minPrev);
                minLastChild.setNext(minNext);
                minNext.setPrev(minLastChild);
            }
            else {
                this.first=minChild;
            }
            NulffyParent(minChild);
        }
        if(first==min){
            first=minNext;
        }
        HeapNode newMin=this.first;
        this.first.getPrev().setNext(null);
        HeapNode temp=this.first;
        while (temp!=null){
            if(temp.getKey()<newMin.getKey())
                newMin=temp;
            temp=temp.getNext();
        }
        this.first.getPrev().setNext(first);
        this.min=newMin;
        this.size-=1;
        Consolidation();
    }
    public void NulffyParent(HeapNode x) //v
    {
        HeapNode temp =  x;
        x.getPrev().setNext(null);
        while(temp!=null) {
            temp.setParent(null);
            if (temp.getMark()) {
                temp.setMark(false);
                NumOfMarked--;
            }
            temp = temp.getNext();
        }
        x.getPrev().setNext(x);
    }
    public void Consolidation() //v
    {
        double GoldenRatio = (1+Math.sqrt(5))/2;
        int length= ((int)(Math.log(this.size)/Math.log(GoldenRatio)))+1;
        HeapNode[] headArr= new HeapNode[length];
        this.first.getPrev().setNext(null);
        HeapNode temp=first;
        HeapNode Next=temp;
        int rank=0;
        while(Next!=null) {
            temp=Next;
            Next=Next.getNext();
            rank = temp.getRank();
            if (headArr[rank] == null)
                headArr[rank] = temp;
            else {
                while (headArr[rank] != null) {
                    temp = link(temp, headArr[rank]);
                    headArr[rank]=null;
                    rank += 1;
                }
                headArr[rank]=temp;
            }
        }
        FibonacciHeap heap1=null;
        FibonacciHeap heap2=null;
        int counter=0;
        for(int i=0;i<length;i++)
        {
            if (headArr[i]!=null){
                headArr[i].setNext(headArr[i]);
                headArr[i].setPrev(headArr[i]);
                headArr[i].setParent(null);
                if (heap1==null) {
                    heap1=new FibonacciHeap(headArr[i]);
                }
                else {
                    heap2 = new FibonacciHeap(headArr[i]);
                    heap1.meld(heap2);
                }
                counter++;
            }
        }
        this.first=heap1.first;
        this.NumOfTrees=counter;
    }

    /**
     * HeapNode link
     * @param Parent : root of a tree
     * @param Child : root of the second tree
     * links two tree with the same degree(D)
     * @return the new tree with degree= D+1
     * Complexity O(1)
     */
    public HeapNode link(HeapNode Parent, HeapNode Child) //v
    {
        if(Parent.getRank()!=Child.getRank()){
            System.out.println("in link func the arguments don't have the same rank");
            return null;
        }
        if (Parent.getKey()>Child.getKey()){
            HeapNode temp=Parent;
            Parent=Child;
            Child=temp;
        }
        if(Parent.getRank()==0) {
            Parent.setChild(Child);
            Child.setParent(Parent);
            Child.setNext(Child);
            Child.setPrev(Child);
            Parent.setRank(1);
        }
        else {
            HeapNode xChild = Parent.getChild();
            HeapNode xLastChild = xChild.getPrev();
            Parent.setChild(Child);
            Child.setParent(Parent);
            Child.setNext(xChild);
            xChild.setPrev(Child);
            Child.setPrev(xLastChild);
            xLastChild.setNext(Child);
            Parent.setRank(Parent.getRank() + 1);
        }
        NumOfTrees--;
        totalLinks++;
        return Parent;
    }

    public void cut(HeapNode Child, HeapNode Parent) //v
    {
        Child.setParent(null);
        if(Child.getMark()) {
            Child.setMark(false);
            NumOfMarked--;
        }
        Parent.setRank(Parent.getRank()-1);
        if(Child.getNext()==Child){
            Parent.setChild(null);
        }
        else {
            Parent.setChild(Child.getNext());
            Child.getPrev().setNext(Child.getNext());
            Child.getNext().setPrev(Child.getPrev());
        }
        Child.setNext(first);
        Child.setPrev(first.getPrev());
        first.setPrev(Child);
        Child.getPrev().setNext(Child);
        first=Child;
        totalCuts++;
        NumOfTrees++;
    }

    public void cascadingCut(HeapNode Child, HeapNode Parent) //v
    {
        cut(Child,Parent);
        if(Parent.getParent()!=null){
            if(!Parent.getMark()) {
                Parent.setMark(true);
                NumOfMarked+=1;
            }
            else cascadingCut(Parent,Parent.getParent());
        }
    }
    /**
     * public HeapNode findMin()
     *
     * Returns the node of the heap whose key is minimal, or null if the heap is empty.
     *
     */
    public HeapNode findMin() //v
    {
        return this.min;
    }

    /**
     * public void meld (FibonacciHeap heap2)
     *
     * Melds heap2 with the current heap.
     *
     */
    public void meld (FibonacciHeap heap2) //v
    {
        if(heap2.isEmpty())
        {
            return;
        }
        if (this.isEmpty())
        {
            this.NumOfTrees=heap2.NumOfTrees;
            this.size= heap2.size();
            this.min=heap2.min;
            this.first=heap2.first;
            this.NumOfMarked=heap2.NumOfMarked;
        }
        else {
            HeapNode last1= this.first.getPrev();
            HeapNode last2= heap2.first.getPrev();
            HeapNode first1=this.first;
            HeapNode first2= heap2.first;
            first1.setPrev(last2);
            last2.setNext(first1);
            last1.setNext(first2);
            first2.setPrev(last1);
            this.NumOfMarked+= heap2.NumOfMarked;
            this.NumOfTrees+= heap2.NumOfTrees;
            this.size+= heap2.size();
            if (this.min.getKey() > heap2.min.getKey()) {
                this.min = heap2.min;
            }
        }
    }

    /**
     * public int size()
     *
     * Returns the number of elements in the heap.
     *
     */
    public int size() //v
    {
        return this.size;
    }

    /**
     * public int[] countersRep()
     *
     * Return an array of counters. The i-th entry contains the number of trees of order i in the heap.
     * Note: The size of of the array depends on the maximum order of a tree, and an empty heap returns an empty array.
     *
     */
    public int[] countersRep() //v
    {
        if(this.size==0){
            return new int[0];
        }
        int length=this.first.rank;
        HeapNode tmp= this.first;
        this.first.getPrev().setNext(null);
        while(tmp!=null)
        {
            if(tmp.getRank()>length){
                length=tmp.getRank();
            }
            tmp=tmp.getNext();
        }
        int[] arr = new int[length+1];
        tmp= this.first;
        this.first.getPrev().setNext(null);
        while(tmp!=null)
        {
            arr[tmp.rank]++;
            tmp=tmp.next;
        }
        this.first.getPrev().setNext(this.first);
        return arr;
    }

    /**
     * public void delete(HeapNode x)
     *
     * Deletes the node x from the heap.
     * It is assumed that x indeed belongs to the heap.
     *
     */
    public void delete(HeapNode x) //v
    {
        int y=this.min.key;
        if (x.key==y)
        {
            deleteMin();
            return;
        }
        y=x.key-y;
        decreaseKey(x,y+13);
        deleteMin();
    }

    /**
     * public void decreaseKey(HeapNode x, int delta)
     *
     * Decreases the key of the node x by a non-negative value delta. The structure of the heap should be updated
     * to reflect this change (for example, the cascading cuts procedure should be applied if needed).
     */
    public void decreaseKey(HeapNode x, int delta) //v
    {
        if (x.getKey()==min.getKey())
        {
            x.setKey(x.getKey()-delta);
            return;
        }
        if (x.getParent()==null) {
            x.setKey(x.getKey() - delta);
            if(x.getKey()<this.min.getKey())
                this.min=x;
            return;
        }
        x.setKey(x.getKey()-delta);
        if (x.getKey()>x.getParent().getKey())
        {
            return;
        }
        else
        {
            if (x.getParent()!=null)
            {
                cascadingCut(x,x.getParent());
            }
        }
        if (x.getKey()<this.min.getKey())
        {
            this.min=x;
        }
        return;

    }

    /**
     * public int potential()
     *
     * This function returns the current potential of the heap, which is:
     * Potential = #trees + 2*#marked
     *
     * In words: The potential equals to the number of trees in the heap
     * plus twice the number of marked nodes in the heap.
     */
    public int potential() //v
    {
        return NumOfTrees+2*NumOfMarked;
    }

    /**
     * public static int totalLinks()
     *
     * This static function returns the total number of link operations made during the
     * run-time of the program. A link operation is the operation which gets as input two
     * trees of the same rank, and generates a tree of rank bigger by one, by hanging the
     * tree which has larger value in its root under the other tree.
     */
    public static int totalLinks() //v
    {
        return totalLinks;
    }

    /**
     * public static int totalCuts()
     *
     * This static function returns the total number of cut operations made during the
     * run-time of the program. A cut operation is the operation which disconnects a subtree
     * from its parent (during decreaseKey/delete methods).
     */
    public static int totalCuts() //v
    {
        return totalCuts;
    }

    /**
     * public static int[] kMin(FibonacciHeap H, int k)
     *
     * This static function returns the k smallest elements in a Fibonacci heap that contains a single tree.
     * The function should run in O(k*deg(H)). (deg(H) is the degree of the only tree in H.)
     *
     * ###CRITICAL### : you are NOT allowed to change H.
     */
    public static int[] kMin(FibonacciHeap H, int k) //v
    {
        HeapNode[] Reserved = new HeapNode[H.size()];
        int Last=0;
        int minLocation=0;
        int[] arr= new int[k-1];
        Reserved[0]=H.getFirst();
        HeapNode CurrMin= H.first;
        FibonacciHeap HelpHeap= new FibonacciHeap();
        HelpHeap.insert(H.first.getKey());
        HeapNode temp=null;
        for(int i=1;i<k;i++){
            arr[i-1]=HelpHeap.findMin().getKey();
            HelpHeap.deleteMin();
            temp=CurrMin.getChild();
            for(int j=0;j< CurrMin.getRank();j++){
                Last++;
                Reserved[Last]=temp;
                HelpHeap.insert(temp.getKey());
                temp=temp.getNext();
            }
            Reserved[minLocation]=Reserved[Last];
            Last--;
            CurrMin=Reserved[0];
            for (int j=0;j<=Last;j++){
                if(Reserved[j].getKey()< CurrMin.getKey()){
                    CurrMin=Reserved[j];
                    minLocation=j;
                }
            }
        }
        return arr;
    }

    public HeapNode getFirst()
    {
        return this.first;
    }

    /**
     * public class HeapNode
     *
     * If you wish to implement classes other than FibonacciHeap
     * (for example HeapNode), do it in this file, not in another file.
     *
     */
    public static class HeapNode{

        public int key;
        private int rank;
        private boolean mark;
        private HeapNode child;
        private HeapNode parent;
        private HeapNode next;
        private HeapNode prev;


        public HeapNode(int key) {
            this.key = key;
            this.mark=false;
        }

        public int getKey() {
            return this.key;
        }

        public void setKey(int key){ this.key=key;}

        public void setMark(boolean mark) {
            this.mark = mark;
        }

        public boolean getMark() {
            return mark;
        }

        public void setChild(HeapNode child) {
            this.child = child;
        }

        public HeapNode getChild() {
            return child;
        }

        public void setNext(HeapNode next) {
            this.next = next;
        }

        public HeapNode getNext() {
            return next;
        }

        public void setParent(HeapNode parent) {
            this.parent = parent;
        }

        public HeapNode getParent() {
            return parent;
        }

        public void setPrev(HeapNode prev) {
            this.prev = prev;
        }

        public HeapNode getPrev() {
            return prev;
        }

        public void setRank(int rank) {
            this.rank = rank;
        }

        public int getRank() {
            return rank;
        }
    }
}
