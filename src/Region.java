public record Region(int id, String name, String description) {
    public Region {
        if (id < 0) {
            throw new IllegalArgumentException("Monster id can't be negative.");
        }

        if (name.isEmpty()) {
            throw new IllegalArgumentException("Must provide a name.");
        }
    }
}
