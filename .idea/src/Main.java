// Abstrakcyjna klasa Shape
abstract class Shape {
    // Abstrakcyjna metoda area()
    public abstract double area();

    // Metoda circumference(), zwracająca 0.0
    public double circumference() {
        return 0.0;
    }
}

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

// Klasa Circle dziedzicząca po Shape
class Circle extends Shape {
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

// Klasa Square dziedzicząca po Shape
class Square extends Shape {
    private double sideLength;

    public Square(double sideLength) {
        this.sideLength = sideLength;
    }

    public double area() {
        return sideLength * sideLength;
    }

    public double circumference() {
        return 4 * sideLength;
    }
}

// Klasa Triangle dziedzicząca po Shape
class Triangle extends Shape {
    private double base;
    private double height;

    public Triangle(double base, double height) {
        this.base = base;
        this.height = height;
    }

    public double area() {
        return 0.5 * base * height;
    }
}

// Klasa Calculator z metodą calculateAreaSum
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

    // Metoda obliczająca sumę powierzchni dwóch figur
    public double calculateAreaSum(Shape shape1, Shape shape2) {
        return shape1.area() + shape2.area();
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

        Square square = new Square(5);
        System.out.println("Pole powierzchni kwadratu: " + square.area());
        System.out.println("Obwód kwadratu: " + square.circumference());

        Triangle triangle = new Triangle(5, 8);
        System.out.println("Pole powierzchni trójkąta: " + triangle.area());

        // Obliczenie sumy powierzchni dwóch figur
        System.out.println("Suma powierzchni okręgu i kwadratu: " + calculator.calculateAreaSum(circle, square));
        System.out.println("Suma powierzchni kwadratu i trójkąta: " + calculator.calculateAreaSum(square, triangle));
    }
}
