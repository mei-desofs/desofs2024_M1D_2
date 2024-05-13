import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { PortfolioMySuffixService } from '../service/portfolio-my-suffix.service';
import { IPortfolioMySuffix } from '../portfolio-my-suffix.model';
import { PortfolioMySuffixFormService } from './portfolio-my-suffix-form.service';

import { PortfolioMySuffixUpdateComponent } from './portfolio-my-suffix-update.component';

describe('PortfolioMySuffix Management Update Component', () => {
  let comp: PortfolioMySuffixUpdateComponent;
  let fixture: ComponentFixture<PortfolioMySuffixUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let portfolioFormService: PortfolioMySuffixFormService;
  let portfolioService: PortfolioMySuffixService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, PortfolioMySuffixUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(PortfolioMySuffixUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PortfolioMySuffixUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    portfolioFormService = TestBed.inject(PortfolioMySuffixFormService);
    portfolioService = TestBed.inject(PortfolioMySuffixService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const portfolio: IPortfolioMySuffix = { id: 456 };

      activatedRoute.data = of({ portfolio });
      comp.ngOnInit();

      expect(comp.portfolio).toEqual(portfolio);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPortfolioMySuffix>>();
      const portfolio = { id: 123 };
      jest.spyOn(portfolioFormService, 'getPortfolioMySuffix').mockReturnValue(portfolio);
      jest.spyOn(portfolioService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ portfolio });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: portfolio }));
      saveSubject.complete();

      // THEN
      expect(portfolioFormService.getPortfolioMySuffix).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(portfolioService.update).toHaveBeenCalledWith(expect.objectContaining(portfolio));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPortfolioMySuffix>>();
      const portfolio = { id: 123 };
      jest.spyOn(portfolioFormService, 'getPortfolioMySuffix').mockReturnValue({ id: null });
      jest.spyOn(portfolioService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ portfolio: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: portfolio }));
      saveSubject.complete();

      // THEN
      expect(portfolioFormService.getPortfolioMySuffix).toHaveBeenCalled();
      expect(portfolioService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPortfolioMySuffix>>();
      const portfolio = { id: 123 };
      jest.spyOn(portfolioService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ portfolio });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(portfolioService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
