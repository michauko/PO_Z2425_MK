class Point {
    double x;
    double y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}

class Calculator {
    public double distance(Point a, Point b) {
        double deltaX = b.getX() - a.getX();
        double deltaY = b.getY() - a.getY();
        return Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    }

    public double distanceX(Point a, Point b) {
        return Math.abs(b.getX() - a.getX());
    }

    public double distanceY(Point a, Point b) {
        return Math.abs(b.getY() - a.getY());
    }
}

class Circle {
    private Point center;
    private double diameter;

    public Circle(Point center, double diameter) {
        this.center = center;
        this.diameter = diameter;
    }

    public double area() {
        double radius = diameter / 2;
        return Math.PI * radius * radius;
    }

    public double circumference() {
        return Math.PI * diameter;
    }
}

public class Main {
    public static void main(String[] args) {
        Point pointA = new Point(5, 4);
        Point pointB = new Point(3, 7);

        Calculator calculator = new Calculator();

        System.out.println("Odległość: " + calculator.distance(pointA, pointB));
        System.out.println("Odległość wzdłuż osi X: " + calculator.distanceX(pointA, pointB));
        System.out.println("Odległość wzdłuż osi Y: " + calculator.distanceY(pointA, pointB));

        Circle circle = new Circle(new Point(4, 4), 5);
        System.out.println("Pole powierzchni okręgu: " + circle.area());
        System.out.println("Obwód okręgu: " + circle.circumference());
    }
}