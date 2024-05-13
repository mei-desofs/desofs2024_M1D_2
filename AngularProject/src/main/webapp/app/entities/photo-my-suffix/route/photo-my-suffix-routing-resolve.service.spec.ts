import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { of } from 'rxjs';

import { IPhotoMySuffix } from '../photo-my-suffix.model';
import { PhotoMySuffixService } from '../service/photo-my-suffix.service';

import photoResolve from './photo-my-suffix-routing-resolve.service';

describe('PhotoMySuffix routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let service: PhotoMySuffixService;
  let resultPhotoMySuffix: IPhotoMySuffix | null | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({}),
            },
          },
        },
      ],
    });
    mockRouter = TestBed.inject(Router);
    jest.spyOn(mockRouter, 'navigate').mockImplementation(() => Promise.resolve(true));
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRoute).snapshot;
    service = TestBed.inject(PhotoMySuffixService);
    resultPhotoMySuffix = undefined;
  });

  describe('resolve', () => {
    it('should return IPhotoMySuffix returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      TestBed.runInInjectionContext(() => {
        photoResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultPhotoMySuffix = result;
          },
        });
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultPhotoMySuffix).toEqual({ id: 123 });
    });

    it('should return null if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      TestBed.runInInjectionContext(() => {
        photoResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultPhotoMySuffix = result;
          },
        });
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultPhotoMySuffix).toEqual(null);
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse<IPhotoMySuffix>({ body: null })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      TestBed.runInInjectionContext(() => {
        photoResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultPhotoMySuffix = result;
          },
        });
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultPhotoMySuffix).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
