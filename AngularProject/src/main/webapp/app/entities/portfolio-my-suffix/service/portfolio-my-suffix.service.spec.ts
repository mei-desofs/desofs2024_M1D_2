import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IPortfolioMySuffix } from '../portfolio-my-suffix.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../portfolio-my-suffix.test-samples';

import { PortfolioMySuffixService, RestPortfolioMySuffix } from './portfolio-my-suffix.service';

const requireRestSample: RestPortfolioMySuffix = {
  ...sampleWithRequiredData,
  date: sampleWithRequiredData.date?.format(DATE_FORMAT),
};

describe('PortfolioMySuffix Service', () => {
  let service: PortfolioMySuffixService;
  let httpMock: HttpTestingController;
  let expectedResult: IPortfolioMySuffix | IPortfolioMySuffix[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PortfolioMySuffixService);
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

    it('should create a PortfolioMySuffix', () => {
      const portfolio = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(portfolio).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a PortfolioMySuffix', () => {
      const portfolio = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(portfolio).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a PortfolioMySuffix', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of PortfolioMySuffix', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a PortfolioMySuffix', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addPortfolioMySuffixToCollectionIfMissing', () => {
      it('should add a PortfolioMySuffix to an empty array', () => {
        const portfolio: IPortfolioMySuffix = sampleWithRequiredData;
        expectedResult = service.addPortfolioMySuffixToCollectionIfMissing([], portfolio);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(portfolio);
      });

      it('should not add a PortfolioMySuffix to an array that contains it', () => {
        const portfolio: IPortfolioMySuffix = sampleWithRequiredData;
        const portfolioCollection: IPortfolioMySuffix[] = [
          {
            ...portfolio,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addPortfolioMySuffixToCollectionIfMissing(portfolioCollection, portfolio);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PortfolioMySuffix to an array that doesn't contain it", () => {
        const portfolio: IPortfolioMySuffix = sampleWithRequiredData;
        const portfolioCollection: IPortfolioMySuffix[] = [sampleWithPartialData];
        expectedResult = service.addPortfolioMySuffixToCollectionIfMissing(portfolioCollection, portfolio);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(portfolio);
      });

      it('should add only unique PortfolioMySuffix to an array', () => {
        const portfolioArray: IPortfolioMySuffix[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const portfolioCollection: IPortfolioMySuffix[] = [sampleWithRequiredData];
        expectedResult = service.addPortfolioMySuffixToCollectionIfMissing(portfolioCollection, ...portfolioArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const portfolio: IPortfolioMySuffix = sampleWithRequiredData;
        const portfolio2: IPortfolioMySuffix = sampleWithPartialData;
        expectedResult = service.addPortfolioMySuffixToCollectionIfMissing([], portfolio, portfolio2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(portfolio);
        expect(expectedResult).toContain(portfolio2);
      });

      it('should accept null and undefined values', () => {
        const portfolio: IPortfolioMySuffix = sampleWithRequiredData;
        expectedResult = service.addPortfolioMySuffixToCollectionIfMissing([], null, portfolio, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(portfolio);
      });

      it('should return initial array if no PortfolioMySuffix is added', () => {
        const portfolioCollection: IPortfolioMySuffix[] = [sampleWithRequiredData];
        expectedResult = service.addPortfolioMySuffixToCollectionIfMissing(portfolioCollection, undefined, null);
        expect(expectedResult).toEqual(portfolioCollection);
      });
    });

    describe('comparePortfolioMySuffix', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.comparePortfolioMySuffix(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.comparePortfolioMySuffix(entity1, entity2);
        const compareResult2 = service.comparePortfolioMySuffix(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.comparePortfolioMySuffix(entity1, entity2);
        const compareResult2 = service.comparePortfolioMySuffix(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.comparePortfolioMySuffix(entity1, entity2);
        const compareResult2 = service.comparePortfolioMySuffix(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
