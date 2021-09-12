import java.util.Scanner;
import java.io.*;

public class HashTable<T> {
    private NGen<T>[] arr;
    public HashTable() {
        arr = new NGen[100];
    }
    public HashTable(int tableSize) {
        arr = new NGen[tableSize];
    }
    public int hashOne(T key) {
        int length = key.toString().length();
        int hash = 0;
        for (int i=0; i < length; i++) {
            hash += key.toString().charAt(i); //getting the char value for each character
        }
        //we use modulus arithmetics to avoid being out of bounds below
        return hash % arr.length;
    }
    public int hashTwo(T key) {
        int hash = 0;
        int offset = 5;
        char characters[] = key.toString().toCharArray();
        int len = key.toString().length();
        for (int i = 0; i < len; i++) {
            offset++;
            hash += 31 * hash + characters[offset % len];
        }
        if (hash < 0) {
            hash = -hash % arr.length; //the char values can range from -128 to 127, so this makes sure that the hash is always positive
        }
        else {
            hash = hash % arr.length;
        }
        return hash;

    }
    public int hashThree(T key) {
        int length = key.toString().length();
        if (length == 1) {
            return key.toString().charAt(0) % arr.length;
        }
        else {
            //we use the the char value for the first and last letters of the string
            //and modular arithmetic to avoid being out of bounds.
            // we use 11 because it is a prime number so the hash will be more evenly distributed
            return (11 * (key.toString().charAt(0) + key.toString().charAt(length-1))) % arr.length;
        }
    }
    public int specificHash(T key) {
        int length = key.toString().length();
        int hash = 101;

        if (length == 1) {
            return (1 + key.toString().charAt(0)) % arr.length; //
        }

        else {
            for (int i = 0; i < length; i++) {
                hash+= key.toString().charAt(i); // sum all char values in a key
            }
            return (length + hash + key.toString().charAt(length-1)) % arr.length; // sum of all char values + length + char value of last character in the key
        }
    }

    public void add(T data, String method) {
        if (data == null) {
            return;
        }
        int hash = 0;
        if (method.equals("hashOne")) {
            hash = hashOne(data);
        }
        else if (method.equals("hashTwo")) {
            hash = hashTwo(data);
        }
        else if (method.equals("hashThree")) {
            hash = hashThree(data);
        }
        else if (method.equals("specificHash")) {
            hash = specificHash(data);
        }

        //headed linked list implementation
        NGen<T> head = new NGen<>(null, null);
        NGen<T> list = arr[hash];
        NGen<T> node = new NGen<>(data, null);
        head.setNext(list);

        if (list==null) { // if there is no collision, this will be the first node in the linked list
            arr[hash] = node;
        }
        //handling collision using chaining
        else {
            head.setNext(node); // add the node to the front of the linked list
            node.setNext(list);
            while (list != null){
                if (list.getData().equals(node.getData())) { // if there is a duplicate key mapping to the same hash
                    return;
                }
                list = list.getNext();
            }
            arr[hash] = head.getNext();
        }
    }
    public void display() {
        int nonemptyIndices = 0;
        int emptyIndices = 0;
        int uniqueTokens = 0;
        int longestLength = 0;
        int currentLength;
        NGen<T> list = new NGen<T>();
        for (int i=0; i<arr.length; i++) {
            list = arr[i];
            currentLength=0; // total number of nodes in the arr[i] linked list
            if (list!=null) {
                nonemptyIndices++;
                while (list != null) {//counts chain(nodes) of this linked list
                    currentLength++;
                    if (currentLength > longestLength) {
                        longestLength = currentLength;
                    }
                    list = list.getNext();
                    uniqueTokens++;
                }
                System.out.println(i+": " + currentLength);
            }
            else {
                emptyIndices++;
                System.out.println(i+": 0");
            }
            System.out.println();
        }
        int avgCollisionLength = uniqueTokens/nonemptyIndices;
        System.out.println("average collisions: " + avgCollisionLength);
        System.out.println("longest collisions: " + longestLength);
        System.out.println("total unique tokens: " + uniqueTokens);
        System.out.println("empty indices: " + emptyIndices);
        System.out.println("nonempty indices: " + nonemptyIndices);
    }

    public static void main(String[] args) {
        HashTable table = new HashTable();
        HashTable specificHashTable = new HashTable(227);
        Scanner s = new Scanner(System.in);
        Scanner input = new Scanner(System.in);
        String str;
        File f;
        System.out.println("Which file do you want to use for testing out HashTables?" );
        String filename = input.next();
        System.out.println("Which out of the 4 hash functions would you like to use? Choose between hashOne, hashTwo, hashThree, specificHash: ");
        String userInput = input.next();
        try {
            f = new File(filename);
            s = new Scanner(f);
        }
        catch (FileNotFoundException e) {
            System.out.println("File: " + filename + " not found");
        }
        System.out.println("Connection to file: " + filename + " successful");

        System.out.println();
        while (s.hasNext()) {
            str = s.next();
            if (userInput.equals("specificHash")) {
                specificHashTable.add(str,userInput);
            }
            else {
                table.add(str, userInput);
            }
        }
        if (userInput.equals("specificHash")) {
            specificHashTable.display();
        }
        else {
            table.display();
        }
    }

}