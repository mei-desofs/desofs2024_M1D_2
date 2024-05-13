import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IReceiptMySuffix } from '../receipt-my-suffix.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../receipt-my-suffix.test-samples';

import { ReceiptMySuffixService } from './receipt-my-suffix.service';

const requireRestSample: IReceiptMySuffix = {
  ...sampleWithRequiredData,
};

describe('ReceiptMySuffix Service', () => {
  let service: ReceiptMySuffixService;
  let httpMock: HttpTestingController;
  let expectedResult: IReceiptMySuffix | IReceiptMySuffix[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ReceiptMySuffixService);
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

    it('should create a ReceiptMySuffix', () => {
      const receipt = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(receipt).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ReceiptMySuffix', () => {
      const receipt = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(receipt).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ReceiptMySuffix', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ReceiptMySuffix', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ReceiptMySuffix', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addReceiptMySuffixToCollectionIfMissing', () => {
      it('should add a ReceiptMySuffix to an empty array', () => {
        const receipt: IReceiptMySuffix = sampleWithRequiredData;
        expectedResult = service.addReceiptMySuffixToCollectionIfMissing([], receipt);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(receipt);
      });

      it('should not add a ReceiptMySuffix to an array that contains it', () => {
        const receipt: IReceiptMySuffix = sampleWithRequiredData;
        const receiptCollection: IReceiptMySuffix[] = [
          {
            ...receipt,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addReceiptMySuffixToCollectionIfMissing(receiptCollection, receipt);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ReceiptMySuffix to an array that doesn't contain it", () => {
        const receipt: IReceiptMySuffix = sampleWithRequiredData;
        const receiptCollection: IReceiptMySuffix[] = [sampleWithPartialData];
        expectedResult = service.addReceiptMySuffixToCollectionIfMissing(receiptCollection, receipt);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(receipt);
      });

      it('should add only unique ReceiptMySuffix to an array', () => {
        const receiptArray: IReceiptMySuffix[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const receiptCollection: IReceiptMySuffix[] = [sampleWithRequiredData];
        expectedResult = service.addReceiptMySuffixToCollectionIfMissing(receiptCollection, ...receiptArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const receipt: IReceiptMySuffix = sampleWithRequiredData;
        const receipt2: IReceiptMySuffix = sampleWithPartialData;
        expectedResult = service.addReceiptMySuffixToCollectionIfMissing([], receipt, receipt2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(receipt);
        expect(expectedResult).toContain(receipt2);
      });

      it('should accept null and undefined values', () => {
        const receipt: IReceiptMySuffix = sampleWithRequiredData;
        expectedResult = service.addReceiptMySuffixToCollectionIfMissing([], null, receipt, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(receipt);
      });

      it('should return initial array if no ReceiptMySuffix is added', () => {
        const receiptCollection: IReceiptMySuffix[] = [sampleWithRequiredData];
        expectedResult = service.addReceiptMySuffixToCollectionIfMissing(receiptCollection, undefined, null);
        expect(expectedResult).toEqual(receiptCollection);
      });
    });

    describe('compareReceiptMySuffix', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareReceiptMySuffix(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareReceiptMySuffix(entity1, entity2);
        const compareResult2 = service.compareReceiptMySuffix(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareReceiptMySuffix(entity1, entity2);
        const compareResult2 = service.compareReceiptMySuffix(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareReceiptMySuffix(entity1, entity2);
        const compareResult2 = service.compareReceiptMySuffix(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
