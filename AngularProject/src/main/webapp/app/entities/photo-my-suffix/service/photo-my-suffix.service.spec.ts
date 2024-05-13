import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IPhotoMySuffix } from '../photo-my-suffix.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../photo-my-suffix.test-samples';

import { PhotoMySuffixService, RestPhotoMySuffix } from './photo-my-suffix.service';

const requireRestSample: RestPhotoMySuffix = {
  ...sampleWithRequiredData,
  date: sampleWithRequiredData.date?.format(DATE_FORMAT),
};

describe('PhotoMySuffix Service', () => {
  let service: PhotoMySuffixService;
  let httpMock: HttpTestingController;
  let expectedResult: IPhotoMySuffix | IPhotoMySuffix[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PhotoMySuffixService);
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

    it('should create a PhotoMySuffix', () => {
      const photo = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(photo).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a PhotoMySuffix', () => {
      const photo = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(photo).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a PhotoMySuffix', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of PhotoMySuffix', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a PhotoMySuffix', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addPhotoMySuffixToCollectionIfMissing', () => {
      it('should add a PhotoMySuffix to an empty array', () => {
        const photo: IPhotoMySuffix = sampleWithRequiredData;
        expectedResult = service.addPhotoMySuffixToCollectionIfMissing([], photo);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(photo);
      });

      it('should not add a PhotoMySuffix to an array that contains it', () => {
        const photo: IPhotoMySuffix = sampleWithRequiredData;
        const photoCollection: IPhotoMySuffix[] = [
          {
            ...photo,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addPhotoMySuffixToCollectionIfMissing(photoCollection, photo);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PhotoMySuffix to an array that doesn't contain it", () => {
        const photo: IPhotoMySuffix = sampleWithRequiredData;
        const photoCollection: IPhotoMySuffix[] = [sampleWithPartialData];
        expectedResult = service.addPhotoMySuffixToCollectionIfMissing(photoCollection, photo);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(photo);
      });

      it('should add only unique PhotoMySuffix to an array', () => {
        const photoArray: IPhotoMySuffix[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const photoCollection: IPhotoMySuffix[] = [sampleWithRequiredData];
        expectedResult = service.addPhotoMySuffixToCollectionIfMissing(photoCollection, ...photoArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const photo: IPhotoMySuffix = sampleWithRequiredData;
        const photo2: IPhotoMySuffix = sampleWithPartialData;
        expectedResult = service.addPhotoMySuffixToCollectionIfMissing([], photo, photo2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(photo);
        expect(expectedResult).toContain(photo2);
      });

      it('should accept null and undefined values', () => {
        const photo: IPhotoMySuffix = sampleWithRequiredData;
        expectedResult = service.addPhotoMySuffixToCollectionIfMissing([], null, photo, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(photo);
      });

      it('should return initial array if no PhotoMySuffix is added', () => {
        const photoCollection: IPhotoMySuffix[] = [sampleWithRequiredData];
        expectedResult = service.addPhotoMySuffixToCollectionIfMissing(photoCollection, undefined, null);
        expect(expectedResult).toEqual(photoCollection);
      });
    });

    describe('comparePhotoMySuffix', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.comparePhotoMySuffix(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.comparePhotoMySuffix(entity1, entity2);
        const compareResult2 = service.comparePhotoMySuffix(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.comparePhotoMySuffix(entity1, entity2);
        const compareResult2 = service.comparePhotoMySuffix(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.comparePhotoMySuffix(entity1, entity2);
        const compareResult2 = service.comparePhotoMySuffix(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
