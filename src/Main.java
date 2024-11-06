// Abstrakcyjna klasa Shape
abstract class Shape {
    // Abstrakcyjna metoda area()
    public abstract double area();

    // Metoda circumference(), zwracająca 0.0
    public double circumference() {
        return 0.0;
    }
}

// Klasa wyjątku BadShapeException
class BadShapeException extends Exception {
    public BadShapeException(String message) {
        super(message);
        System.out.println("Błąd: " + message); // Wyświetlenie komunikatu błędu w konsoli
    }
}

// Klasa wyjątku BadShapeTwoException dziedzicząca po BadShapeException
class BadShapeTwoException extends BadShapeException {
    public BadShapeTwoException(String message) {
        super(message);
    }
}

class Point {
    double x;
    double y;

    public Point(double x, double y) throws BadShapeException {
        if (x < 0 || y < 0) {
            throw new BadShapeException("Współrzędne punktu nie mogą być ujemne.");
        }
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
        try {
            if (diameter < 1) {
                throw new BadShapeTwoException("Średnica okręgu musi być większa lub równa 1.");
            }
            this.center = center;
            this.diameter = diameter;
        } catch (BadShapeTwoException e) {
            System.out.println("Obsługa wyjątku: " + e.getMessage());
        } catch (BadShapeException e) {
            System.out.println("Obsługa wyjątku: " + e.getMessage());
        } finally {
            System.out.println("Koniec pracy konstruktora Circle.");
        }
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

    public Square(double sideLength) throws BadShapeException {
        if (sideLength <= 0) {
            throw new BadShapeException("Długość boku kwadratu musi być większa od 0.");
        }
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

    public Triangle(double base, double height) throws BadShapeException {
        if (base <= 0 || height <= 0) {
            throw new BadShapeException("Podstawa i wysokość trójkąta muszą być większe od 0.");
        }
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
        try {
            Point pointA = new Point(5, 4);
            Point pointB = new Point(3, 7);

            Calculator calculator = new Calculator();

            System.out.println("Odległość: " + calculator.distance(pointA, pointB));
            System.out.println("Odległość wzdłuż osi X: " + calculator.distanceX(pointA, pointB));
            System.out.println("Odległość wzdłuż osi Y: " + calculator.distanceY(pointA, pointB));

            Circle circle = new Circle(new Point(4, 4), 0.5); // Próba ustawienia złej średnicy
            System.out.println("Pole powierzchni okręgu: " + circle.area());
            System.out.println("Obwód okręgu: " + circle.circumference());

            Square square = new Square(5);
            System.out.println("Pole powierzchni kwadratu: " + square.area());
            System.out.println("Obwód kwadratu: " + square.circumference());

            Triangle triangle = new Triangle(0, 8);
            System.out.println("Pole powierzchni trójkąta: " + triangle.area());

            // Obliczenie sumy powierzchni dwóch figur
            System.out.println("Suma powierzchni okręgu i kwadratu: " + calculator.calculateAreaSum(circle, square));
            System.out.println("Suma powierzchni kwadratu i trójkąta: " + calculator.calculateAreaSum(square, triangle));
        } catch (BadShapeException e) {
            System.out.println("Obsługa wyjątku w main: " + e.getMessage());
        } finally {
            System.out.println("Zakończenie pracy programu.");
        }
    }
}
