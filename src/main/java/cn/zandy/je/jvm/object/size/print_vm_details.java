package cn.zandy.je.jvm.object.size;

import org.openjdk.jol.vm.VM;

public class print_vm_details {

    public static void main(String[] args) {
        System.out.println(VM.current().details());
    }

    /* by java 1.8.0_112, org.openjdk.jol:jol-core:0.9
---------------------------------------
-XX:+PrintCommandLineFlags -XX:+UseCompressedClassPointers -XX:+UseCompressedOops
                                    or
-XX:+PrintCommandLineFlags -XX:-UseCompressedClassPointers -XX:+UseCompressedOops

# Running 64-bit HotSpot VM.
# Using compressed oop with 3-bit shift.
# Using compressed klass with 3-bit shift.
# WARNING | Compressed references base/shifts are guessed by the experiment!
# WARNING | Therefore, computed addresses are just guesses, and ARE NOT RELIABLE.
# WARNING | Make sure to attach Serviceability Agent to get the reliable addresses.
# Objects are 8 bytes aligned.
# Field sizes by type: 4, 1, 1, 2, 2, 4, 4, 8, 8 [bytes]
# Array element sizes: 4, 1, 1, 2, 2, 4, 4, 8, 8 [bytes]

---------------------------------------
-XX:+PrintCommandLineFlags -XX:-UseCompressedOops

# Running 64-bit HotSpot VM.
# Objects are 8 bytes aligned.
# Field sizes by type: 8, 1, 1, 2, 2, 4, 4, 8, 8 [bytes]
# Array element sizes: 8, 1, 1, 2, 2, 4, 4, 8, 8 [bytes]
     */
}
