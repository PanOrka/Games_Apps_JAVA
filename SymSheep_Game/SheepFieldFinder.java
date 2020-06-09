import java.util.*;

public class SheepFieldFinder {
private int _x, _y, _sheepX, _sheepY, _wolfX, _wolfY, _distance, _howSame, _rng;
private Owolf _wolf;
private Random _rand;
private boolean _switcher, _done;
private int[] _placeHolderX, _placeHolderY, _whichPlace;
private java.util.List<Field> _fields;

public SheepFieldFinder(Owolf wolf, int x, int y, java.util.List<Field> fields) {
        this._rand = new Random();
        this._wolf = wolf;
        this._x = x;
        this._y = y;
        this._fields = fields;

        this._placeHolderX = new int[8];
        this._placeHolderY = new int[8];
}

public int Finder(int position) {
        this._howSame = 1;
        this._switcher = false;
        this._done = false;

        this._wolfX = ((this._wolf.position) % this._x) + 1;
        this._wolfY = ((this._wolf.position + 1 - this._wolfX)/this._x) + 1;

        this._sheepX = ((position) % this._x) + 1;
        this._sheepY = ((position + 1 - this._sheepX)/this._x) + 1;

        this._placeHolderX[0] = this._sheepX - 1; this._placeHolderY[0] =this._sheepY - 1;
        this._placeHolderX[1] = this._sheepX; this._placeHolderY[1] = this._sheepY - 1;
        this._placeHolderX[2] = this._sheepX + 1; this._placeHolderY[2] = this._sheepY - 1;
        this._placeHolderX[3] = this._sheepX - 1; this._placeHolderY[3] = this._sheepY;
        this._placeHolderX[4] = this._sheepX + 1; this._placeHolderY[4] = this._sheepY;
        this._placeHolderX[5] = this._sheepX - 1; this._placeHolderY[5] = this._sheepY + 1;
        this._placeHolderX[6] = this._sheepX; this._placeHolderY[6] = this._sheepY + 1;
        this._placeHolderX[7] = this._sheepX + 1; this._placeHolderY[7] = this._sheepY + 1;

        for (int i=0; i<8; i++) {
                if (!this._switcher && !(this._placeHolderX[i] <= 0 || this._placeHolderY[i] <= 0 || this._placeHolderX[i] >= this._x + 1 || this._placeHolderY[i] >= this._y + 1) && this.isFree(i)) {
                        this._distance = this.Distance(i);
                        this._switcher = true;
                        this._done = true;
                } else if (this._distance < this.Distance(i) && !(this._placeHolderX[i] <= 0 || this._placeHolderY[i] <= 0 || this._placeHolderX[i] >= this._x + 1 || this._placeHolderY[i] >= this._y + 1) && this.isFree(i)) {
                        this._distance = this.Distance(i);
                        this._howSame = 1;
                } else if (this._distance == this.Distance(i) && !(this._placeHolderX[i] <= 0 || this._placeHolderY[i] <= 0 || this._placeHolderX[i] >= this._x + 1 || this._placeHolderY[i] >= this._y + 1) && this.isFree(i)) {
                        this._howSame++;
                }
        }

        if (this._howSame != 1) {
                this._rng = this._rand.nextInt(this._howSame-1);
                this._whichPlace = new int[this._howSame];

                this._howSame = 0; // TUTAJ POSLUGUJE SIE JAKO MIEJSCEM TABLICY

                for (int i=0; i<8; i++) {

                        if (this.Distance(i) == this._distance && !(this._placeHolderX[i] <= 0 || this._placeHolderY[i] <= 0 || this._placeHolderX[i] >= this._x + 1 || this._placeHolderY[i] >= this._y + 1) && this.isFree(i)) {
                                this._whichPlace[this._howSame] = i;
                                this._howSame++;
                        }
                }

        } else if (this._done) {
                this._rng = 0;
                this._whichPlace = new int[1];

                for (int i=0; i<8; i++) {

                        if (this.Distance(i) == this._distance && !(this._placeHolderX[i] <= 0 || this._placeHolderY[i] <= 0 || this._placeHolderX[i] >= this._x + 1 || this._placeHolderY[i] >= this._y + 1) && this.isFree(i)) {
                                this._whichPlace[this._rng] = i;
                                break;
                        }
                }
        }

        if (this._done) {
                return (int)(this._placeHolderX[this._whichPlace[this._rng]] + ((this._placeHolderY[this._whichPlace[this._rng]]-1)*this._x) - 1);
        } else return (int)(position);
}

private int Distance(int fNumber) {
        int A, B;
        A = this._wolfX - this._placeHolderX[fNumber];
        B = this._wolfY - this._placeHolderY[fNumber];

        return (int)((A*A)+(B*B));
}

private boolean isFree(int i) {
        int place;
        place = this._placeHolderX[i] + ((this._placeHolderY[i]-1)*this._x) - 1;

        if (this._fields.get(place).fieldBlocker) {
                return true;
        } else return false;
}
}
