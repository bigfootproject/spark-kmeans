package main.feature

@serializable case class Point(x: Double, y: Double) {
  //relaxed operators
  def +(that: Point) = new Point(this.x + that.x, this.y + that.y)
  def -(that: Point) = this + (-that)
  def unary_-() = new Point(-this.x, -this.y)
  def /(d: Double) = new Point(this.x / d, this.y / d)
  //to define according to distance (Euclidean distance)
  def magnitude = math.sqrt(x * x + y * y)
  def distance(that: Point) = (that - this).magnitude

  override def toString = format("(%.2f - %.2f)", x, y)
}

object Point {
  //for random points
  def random() = new Point(math.random * 10, math.random * 10)
}