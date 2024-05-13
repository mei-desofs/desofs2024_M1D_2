import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IRoleMySuffix } from '../role-my-suffix.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../role-my-suffix.test-samples';

import { RoleMySuffixService } from './role-my-suffix.service';

const requireRestSample: IRoleMySuffix = {
  ...sampleWithRequiredData,
};

describe('RoleMySuffix Service', () => {
  let service: RoleMySuffixService;
  let httpMock: HttpTestingController;
  let expectedResult: IRoleMySuffix | IRoleMySuffix[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(RoleMySuffixService);
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

    it('should create a RoleMySuffix', () => {
      const role = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(role).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a RoleMySuffix', () => {
      const role = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(role).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a RoleMySuffix', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of RoleMySuffix', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a RoleMySuffix', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addRoleMySuffixToCollectionIfMissing', () => {
      it('should add a RoleMySuffix to an empty array', () => {
        const role: IRoleMySuffix = sampleWithRequiredData;
        expectedResult = service.addRoleMySuffixToCollectionIfMissing([], role);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(role);
      });

      it('should not add a RoleMySuffix to an array that contains it', () => {
        const role: IRoleMySuffix = sampleWithRequiredData;
        const roleCollection: IRoleMySuffix[] = [
          {
            ...role,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addRoleMySuffixToCollectionIfMissing(roleCollection, role);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a RoleMySuffix to an array that doesn't contain it", () => {
        const role: IRoleMySuffix = sampleWithRequiredData;
        const roleCollection: IRoleMySuffix[] = [sampleWithPartialData];
        expectedResult = service.addRoleMySuffixToCollectionIfMissing(roleCollection, role);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(role);
      });

      it('should add only unique RoleMySuffix to an array', () => {
        const roleArray: IRoleMySuffix[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const roleCollection: IRoleMySuffix[] = [sampleWithRequiredData];
        expectedResult = service.addRoleMySuffixToCollectionIfMissing(roleCollection, ...roleArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const role: IRoleMySuffix = sampleWithRequiredData;
        const role2: IRoleMySuffix = sampleWithPartialData;
        expectedResult = service.addRoleMySuffixToCollectionIfMissing([], role, role2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(role);
        expect(expectedResult).toContain(role2);
      });

      it('should accept null and undefined values', () => {
        const role: IRoleMySuffix = sampleWithRequiredData;
        expectedResult = service.addRoleMySuffixToCollectionIfMissing([], null, role, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(role);
      });

      it('should return initial array if no RoleMySuffix is added', () => {
        const roleCollection: IRoleMySuffix[] = [sampleWithRequiredData];
        expectedResult = service.addRoleMySuffixToCollectionIfMissing(roleCollection, undefined, null);
        expect(expectedResult).toEqual(roleCollection);
      });
    });

    describe('compareRoleMySuffix', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareRoleMySuffix(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareRoleMySuffix(entity1, entity2);
        const compareResult2 = service.compareRoleMySuffix(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareRoleMySuffix(entity1, entity2);
        const compareResult2 = service.compareRoleMySuffix(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareRoleMySuffix(entity1, entity2);
        const compareResult2 = service.compareRoleMySuffix(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
