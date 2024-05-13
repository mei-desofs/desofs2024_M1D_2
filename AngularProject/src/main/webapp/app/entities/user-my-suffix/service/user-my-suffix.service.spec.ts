import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IUserMySuffix } from '../user-my-suffix.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../user-my-suffix.test-samples';

import { UserMySuffixService } from './user-my-suffix.service';

const requireRestSample: IUserMySuffix = {
  ...sampleWithRequiredData,
};

describe('UserMySuffix Service', () => {
  let service: UserMySuffixService;
  let httpMock: HttpTestingController;
  let expectedResult: IUserMySuffix | IUserMySuffix[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(UserMySuffixService);
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

    it('should create a UserMySuffix', () => {
      const user = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(user).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a UserMySuffix', () => {
      const user = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(user).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a UserMySuffix', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of UserMySuffix', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a UserMySuffix', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addUserMySuffixToCollectionIfMissing', () => {
      it('should add a UserMySuffix to an empty array', () => {
        const user: IUserMySuffix = sampleWithRequiredData;
        expectedResult = service.addUserMySuffixToCollectionIfMissing([], user);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(user);
      });

      it('should not add a UserMySuffix to an array that contains it', () => {
        const user: IUserMySuffix = sampleWithRequiredData;
        const userCollection: IUserMySuffix[] = [
          {
            ...user,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addUserMySuffixToCollectionIfMissing(userCollection, user);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a UserMySuffix to an array that doesn't contain it", () => {
        const user: IUserMySuffix = sampleWithRequiredData;
        const userCollection: IUserMySuffix[] = [sampleWithPartialData];
        expectedResult = service.addUserMySuffixToCollectionIfMissing(userCollection, user);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(user);
      });

      it('should add only unique UserMySuffix to an array', () => {
        const userArray: IUserMySuffix[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const userCollection: IUserMySuffix[] = [sampleWithRequiredData];
        expectedResult = service.addUserMySuffixToCollectionIfMissing(userCollection, ...userArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const user: IUserMySuffix = sampleWithRequiredData;
        const user2: IUserMySuffix = sampleWithPartialData;
        expectedResult = service.addUserMySuffixToCollectionIfMissing([], user, user2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(user);
        expect(expectedResult).toContain(user2);
      });

      it('should accept null and undefined values', () => {
        const user: IUserMySuffix = sampleWithRequiredData;
        expectedResult = service.addUserMySuffixToCollectionIfMissing([], null, user, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(user);
      });

      it('should return initial array if no UserMySuffix is added', () => {
        const userCollection: IUserMySuffix[] = [sampleWithRequiredData];
        expectedResult = service.addUserMySuffixToCollectionIfMissing(userCollection, undefined, null);
        expect(expectedResult).toEqual(userCollection);
      });
    });

    describe('compareUserMySuffix', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareUserMySuffix(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareUserMySuffix(entity1, entity2);
        const compareResult2 = service.compareUserMySuffix(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareUserMySuffix(entity1, entity2);
        const compareResult2 = service.compareUserMySuffix(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareUserMySuffix(entity1, entity2);
        const compareResult2 = service.compareUserMySuffix(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
