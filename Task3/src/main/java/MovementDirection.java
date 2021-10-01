public enum MovementDirection {
    UP(1), DOWN(-1);

    private final int floorDelta;

    MovementDirection(int floorDelta) {
        this.floorDelta = floorDelta;
    }

    public int floorDelta() {
        return floorDelta;
    }
}
