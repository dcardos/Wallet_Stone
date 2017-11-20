public final class User {     // Basic User (no specification on assignment)
    private final String mName;
    private final String mCPF;

    public User(String name, String CPF) {
        mName = name;
        mCPF = CPF;
    }

    public String getName() {
        return mName;
    }

    public String getCPF() {
        return mCPF;
    }

    // Accessor functions for mutable objects (correcting wrong values)
    public User changeName(String newName) {
        return new User(newName, mCPF);
    }

    public User changeCPF(String newCPF) {
        return new User(mName, newCPF);
    }
}
