import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { of } from 'rxjs';

import { IPortfolioMySuffix } from '../portfolio-my-suffix.model';
import { PortfolioMySuffixService } from '../service/portfolio-my-suffix.service';

import portfolioResolve from './portfolio-my-suffix-routing-resolve.service';

describe('PortfolioMySuffix routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let service: PortfolioMySuffixService;
  let resultPortfolioMySuffix: IPortfolioMySuffix | null | undefined;

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
    service = TestBed.inject(PortfolioMySuffixService);
    resultPortfolioMySuffix = undefined;
  });

  describe('resolve', () => {
    it('should return IPortfolioMySuffix returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      TestBed.runInInjectionContext(() => {
        portfolioResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultPortfolioMySuffix = result;
          },
        });
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultPortfolioMySuffix).toEqual({ id: 123 });
    });

    it('should return null if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      TestBed.runInInjectionContext(() => {
        portfolioResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultPortfolioMySuffix = result;
          },
        });
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultPortfolioMySuffix).toEqual(null);
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse<IPortfolioMySuffix>({ body: null })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      TestBed.runInInjectionContext(() => {
        portfolioResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultPortfolioMySuffix = result;
          },
        });
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultPortfolioMySuffix).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
