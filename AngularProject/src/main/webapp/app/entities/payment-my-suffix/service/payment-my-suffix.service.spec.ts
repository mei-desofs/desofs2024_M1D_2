import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IPaymentMySuffix } from '../payment-my-suffix.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../payment-my-suffix.test-samples';

import { PaymentMySuffixService, RestPaymentMySuffix } from './payment-my-suffix.service';

const requireRestSample: RestPaymentMySuffix = {
  ...sampleWithRequiredData,
  date: sampleWithRequiredData.date?.format(DATE_FORMAT),
};

describe('PaymentMySuffix Service', () => {
  let service: PaymentMySuffixService;
  let httpMock: HttpTestingController;
  let expectedResult: IPaymentMySuffix | IPaymentMySuffix[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PaymentMySuffixService);
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

    it('should create a PaymentMySuffix', () => {
      const payment = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(payment).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a PaymentMySuffix', () => {
      const payment = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(payment).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a PaymentMySuffix', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of PaymentMySuffix', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a PaymentMySuffix', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addPaymentMySuffixToCollectionIfMissing', () => {
      it('should add a PaymentMySuffix to an empty array', () => {
        const payment: IPaymentMySuffix = sampleWithRequiredData;
        expectedResult = service.addPaymentMySuffixToCollectionIfMissing([], payment);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(payment);
      });

      it('should not add a PaymentMySuffix to an array that contains it', () => {
        const payment: IPaymentMySuffix = sampleWithRequiredData;
        const paymentCollection: IPaymentMySuffix[] = [
          {
            ...payment,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addPaymentMySuffixToCollectionIfMissing(paymentCollection, payment);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PaymentMySuffix to an array that doesn't contain it", () => {
        const payment: IPaymentMySuffix = sampleWithRequiredData;
        const paymentCollection: IPaymentMySuffix[] = [sampleWithPartialData];
        expectedResult = service.addPaymentMySuffixToCollectionIfMissing(paymentCollection, payment);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(payment);
      });

      it('should add only unique PaymentMySuffix to an array', () => {
        const paymentArray: IPaymentMySuffix[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const paymentCollection: IPaymentMySuffix[] = [sampleWithRequiredData];
        expectedResult = service.addPaymentMySuffixToCollectionIfMissing(paymentCollection, ...paymentArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const payment: IPaymentMySuffix = sampleWithRequiredData;
        const payment2: IPaymentMySuffix = sampleWithPartialData;
        expectedResult = service.addPaymentMySuffixToCollectionIfMissing([], payment, payment2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(payment);
        expect(expectedResult).toContain(payment2);
      });

      it('should accept null and undefined values', () => {
        const payment: IPaymentMySuffix = sampleWithRequiredData;
        expectedResult = service.addPaymentMySuffixToCollectionIfMissing([], null, payment, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(payment);
      });

      it('should return initial array if no PaymentMySuffix is added', () => {
        const paymentCollection: IPaymentMySuffix[] = [sampleWithRequiredData];
        expectedResult = service.addPaymentMySuffixToCollectionIfMissing(paymentCollection, undefined, null);
        expect(expectedResult).toEqual(paymentCollection);
      });
    });

    describe('comparePaymentMySuffix', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.comparePaymentMySuffix(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.comparePaymentMySuffix(entity1, entity2);
        const compareResult2 = service.comparePaymentMySuffix(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.comparePaymentMySuffix(entity1, entity2);
        const compareResult2 = service.comparePaymentMySuffix(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.comparePaymentMySuffix(entity1, entity2);
        const compareResult2 = service.comparePaymentMySuffix(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
