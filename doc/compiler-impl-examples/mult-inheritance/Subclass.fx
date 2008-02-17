class Base {
    attribute a : Integer = 3;
}

class OtherBase {
    attribute b : Integer = 4;
}

class Subclass extends Base, OtherBase {
    attribute c : Integer = 5;
}

Subclass { a: 1, b: 2 }
