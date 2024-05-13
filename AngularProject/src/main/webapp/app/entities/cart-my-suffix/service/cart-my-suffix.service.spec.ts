import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ICartMySuffix } from '../cart-my-suffix.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../cart-my-suffix.test-samples';

import { CartMySuffixService } from './cart-my-suffix.service';

const requireRestSample: ICartMySuffix = {
  ...sampleWithRequiredData,
};

describe('CartMySuffix Service', () => {
  let service: CartMySuffixService;
  let httpMock: HttpTestingController;
  let expectedResult: ICartMySuffix | ICartMySuffix[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CartMySuffixService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a CartMySuffix', () => {
      const cart = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(cart).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CartMySuffix', () => {
      const cart = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(cart).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CartMySuffix', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CartMySuffix', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a CartMySuffix', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addCartMySuffixToCollectionIfMissing', () => {
      it('should add a CartMySuffix to an empty array', () => {
        const cart: ICartMySuffix = sampleWithRequiredData;
        expectedResult = service.addCartMySuffixToCollectionIfMissing([], cart);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(cart);
      });

      it('should not add a CartMySuffix to an array that contains it', () => {
        const cart: ICartMySuffix = sampleWithRequiredData;
        const cartCollection: ICartMySuffix[] = [
          {
            ...cart,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addCartMySuffixToCollectionIfMissing(cartCollection, cart);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CartMySuffix to an array that doesn't contain it", () => {
        const cart: ICartMySuffix = sampleWithRequiredData;
        const cartCollection: ICartMySuffix[] = [sampleWithPartialData];
        expectedResult = service.addCartMySuffixToCollectionIfMissing(cartCollection, cart);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(cart);
      });

      it('should add only unique CartMySuffix to an array', () => {
        const cartArray: ICartMySuffix[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const cartCollection: ICartMySuffix[] = [sampleWithRequiredData];
        expectedResult = service.addCartMySuffixToCollectionIfMissing(cartCollection, ...cartArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const cart: ICartMySuffix = sampleWithRequiredData;
        const cart2: ICartMySuffix = sampleWithPartialData;
        expectedResult = service.addCartMySuffixToCollectionIfMissing([], cart, cart2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(cart);
        expect(expectedResult).toContain(cart2);
      });

      it('should accept null and undefined values', () => {
        const cart: ICartMySuffix = sampleWithRequiredData;
        expectedResult = service.addCartMySuffixToCollectionIfMissing([], null, cart, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(cart);
      });

      it('should return initial array if no CartMySuffix is added', () => {
        const cartCollection: ICartMySuffix[] = [sampleWithRequiredData];
        expectedResult = service.addCartMySuffixToCollectionIfMissing(cartCollection, undefined, null);
        expect(expectedResult).toEqual(cartCollection);
      });
    });

    describe('compareCartMySuffix', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareCartMySuffix(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareCartMySuffix(entity1, entity2);
        const compareResult2 = service.compareCartMySuffix(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareCartMySuffix(entity1, entity2);
        const compareResult2 = service.compareCartMySuffix(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareCartMySuffix(entity1, entity2);
        const compareResult2 = service.compareCartMySuffix(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
