package pl.e_science.git.ivahan0788;

public class NativeSorter {
    public Double[] a;
    public Double[] b;
    public Boolean order;

    static {
        System.loadLibrary("nativesorter"); // oczekuje nativesorter.dll (Windows)
    }

    // Metoda JNI – przekazuje tablicę i order jako argumenty
    public native Double[] sort01(Double[] a, Boolean order);

    // Metoda JNI – tylko tablica jako argument, order czytany z pola obiektu Java
    public native Double[] sort02(Double[] a);

    // Metoda JNI – otwiera GUI, użytkownik wpisuje dane, wynik idzie do pól a + order
    public native void sort03();

    // Normalna metoda Java – sortuje `a` zgodnie z `order`, wynik zapisuje do `b`
    public void sort04() {
        if (a == null || order == null) {
            throw new IllegalStateException("Fields 'a' and 'order' must not be null.");
        }
        Double[] copy = a.clone();
        if (order) {
            java.util.Arrays.sort(copy);
        } else {
            java.util.Arrays.sort(copy, java.util.Collections.reverseOrder());
        }
        b = copy;
    }
}

