import java.util.Iterator;
import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;

public class CircularLinkedList<AnyType> implements List<AnyType>
{
	
/*
 * The added method for Assignment 2 that I implemented is: indexOf(AnyType value) at line 134	
 */

  //nested Node class
  private static class Node<AnyType>
  {
    private AnyType data;
    private Node<AnyType> next;

    public Node(AnyType d, Node<AnyType> n)
    {
      setData(d);
      setNext(n);
    }

    public AnyType getData() { return data; }

    public void setData(AnyType d) { data = d; }

    public Node<AnyType> getNext() { return next; }

    public void setNext(Node<AnyType> n) { next = n; }
  } //end nested Node class

  private int theSize;
  private int modCount;
  private Node<AnyType> tail;

  //constructor
  public CircularLinkedList() 
  {
	  tail = new Node<AnyType>(null, null);
	  modCount = 0;
	  theSize=0;
  }

  //clear list by getting rid of data in tail and setting pointer of tail to itself
  public void clear()
  {
	  tail.setData(null);
	  tail.setNext(tail);
	  theSize = 0;
  }

  //return size of the list
  public int size()
  {
	  return theSize;
  }

  //return if list is empty or not
  public boolean isEmpty()
  {
	  return (size()==0);
  }

  //get data from node at a given index
  public AnyType get(int index)
  {
	  Node<AnyType> theNode = getNode(index);
	  return theNode.getData();
  }

  //set data of a node at a given index to something new
  public AnyType set(int index, AnyType newValue)
  {
	  Node<AnyType> nodeSet = getNode(index);
	  AnyType old = nodeSet.getData();
	  
	  nodeSet.setData(newValue);
	  return old;
  }

  //return true if new node was added to list
  public boolean add(AnyType newValue)
  {
	  add(size(), newValue);
	  return true;
  }

  //add new node to list, there are 4 case
  //if the list is empty and you are adding the first node
  //if you want to add the node at index 0 with other nodes in the list
  //if you want to add the node at the end of the list
  //if you want to add the node in the middle of the list;
  public void add(int index, AnyType newValue)
  {
	  if(index==0 && size()==0) addFirst(newValue);
	  else if(index == 0) addBeginning(newValue);
	  else if(index == size()) addLast(newValue);
	  else{
		  Node<AnyType> nextNode = getNode(index);
		  Node<AnyType> prevNode = getNode(index-1);
		  Node<AnyType> newNode = new Node<AnyType>(newValue, nextNode);
		  prevNode.setNext(newNode);
	  }
	   theSize++;
	  modCount++;
  }

  //remove a node at a given index, there are 3 cases
  //if you want to remove the first node of a list
  //if you want to remove the last node of the list
  //if you want to remove a node from the middle of the list
  public AnyType remove(int index)
  {
	 AnyType old = null;
	 if(index==0) old = removeFirst(getNode(index));
	 else if(index==size()-1) old = removeLast(getNode(index));
	 else old = removeNode(getNode(index), getNode(index-1));
	 
	 theSize--;
	 modCount++;
	 return old;
  }

  //rotate the head of the list to the tail of the list, a counter-clock wise rotate
  //set the new tail to the head, pointers remain the name
  public void rotate()
  {
	  Node<AnyType> currHeadNode = getNode(0);
	  tail = currHeadNode;
  }
  
  //added this method for CPU scheduler to get the index of a Process when running SRTF
  public int indexOf(AnyType value){
	  for(int i=0; i<theSize; i++){
		  if(value.equals(getNode(i).getData())) return i;
	  }
	  return -1;
  }

  //returns a new linked list iterator object to iterate through list
  public Iterator<AnyType> iterator()
  {
	  return new LinkedListIterator();    
  }
  
  //get node at a given index
  private Node<AnyType> getNode(int index)
  {
	  return (getNode(index, 0, size()-1));
  }

  //will return node at a given index and check the bounds of the index
  private Node<AnyType> getNode(int index, int lower, int upper)
  {
	    Node<AnyType> currNode;

	    if (index < lower || index > upper)
	      throw new IndexOutOfBoundsException();

	     currNode = tail.getNext();
	     for (int i = 0; i < index; i++) currNode = currNode.getNext();
	    return currNode;
  }
  
  //adding the first node of a list
  private void addFirst(AnyType newString){
	  tail.setData(newString);
	  tail.setNext(tail);
	  
  }
  
  //adding a node at the front of the list
  private void addBeginning(AnyType newString){
	  Node<AnyType> nextNode = getNode(0);
	  Node<AnyType> newNode = new Node<AnyType>(newString, nextNode);
	  tail.setNext(newNode);
  }
  
  //adding a node at the end of the list
  private void addLast(AnyType newString){
	  Node<AnyType> prevNode = getNode(size()-1);
	  Node<AnyType> newNode = new Node<AnyType>(newString, getNode(0));
	  tail = newNode;
	  prevNode.setNext(newNode);
  }
    
  //removing a node at the middle of the list
  private AnyType removeNode(Node<AnyType> currNode, Node<AnyType> prevNode){
		 AnyType old = currNode.getData();
		 prevNode.setNext(currNode.getNext());
		 return old;
  }
  
  //removing a node at the front of the list
  private AnyType removeFirst(Node<AnyType> firstNode){
	  AnyType old = firstNode.getData();
	  tail.setNext(firstNode.getNext());
	  return old;
  }
  
  //removing a node at the end of the list
  private AnyType removeLast(Node<AnyType> lastNode){
	  AnyType old = lastNode.getData();
	  Node<AnyType> secondToLastNode = getNode(size()-2);
	  tail = secondToLastNode;
	  secondToLastNode.setNext(lastNode.getNext());
	  return old;
  }
  

  //LLI class
  private class LinkedListIterator implements Iterator<AnyType>
  {
    private Node<AnyType> previous;
    private Node<AnyType> current;
    private int expectedModCount;
    private boolean okToRemove;

    //constructor of linked list iterator, will set the current node to the first node of the list and the previous will
    //be the node before the head which is the tail node
    LinkedListIterator()
    {
    	current = getNode(0);
    	previous = tail;
    	expectedModCount = modCount;
    	okToRemove = false;
    }

    //list will always have a next node since the tail node points to the head node
    public boolean hasNext()
    {
    	return true;
    }

    //will return the data of the node after the current node in the list
    public AnyType next()
    {
        if (modCount != expectedModCount)
            throw new ConcurrentModificationException();
          if (!hasNext())
            throw new NoSuchElementException();
          
    	AnyType nextVal = current.getData();
    	previous = current;
    	current = current.getNext();
    	okToRemove = true;
    	return nextVal;
    }

    //remove a node from the list
    public void remove()
    {
        if (modCount != expectedModCount)
            throw new ConcurrentModificationException();
          if (!okToRemove)
            throw new IllegalStateException();

          CircularLinkedList.this.removeNode(current, previous);
          expectedModCount++;
          okToRemove = false;

    }
  }// end LLI class
}